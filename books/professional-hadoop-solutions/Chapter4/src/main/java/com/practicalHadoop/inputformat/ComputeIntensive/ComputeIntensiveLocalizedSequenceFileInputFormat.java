package com.practicalHadoop.inputformat.ComputeIntensive;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.StringTokenizer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.hadoop.mapred.ClusterStatus;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;

public class ComputeIntensiveLocalizedSequenceFileInputFormat<K, V> 
								extends SequenceFileInputFormat<K, V> {

	/** 
	 * distribute FileSplits, generated by FileInput format evenly between all
	 * available machines.
	 */ 
	@Override
	public List<InputSplit> getSplits(JobContext job) throws IOException {

		// get splits
		List<InputSplit> originalSplits = super.getSplits(job);
		
		// Get active servers
		String[] servers = getActiveServersList(job);
		if(servers == null)
			return null;
		// reassign splits to active servers
		List<InputSplit> splits = new ArrayList<InputSplit>(originalSplits.size());
		int numSplits = originalSplits.size();
		int currentServer = 0;
		for(int i = 0; i < numSplits; i++, currentServer = getNextServer(currentServer, servers.length)){
			String server = servers[currentServer]; // Current server
			boolean replaced = false;
			// For every remaining split
			for(InputSplit split : originalSplits){
				FileSplit fs = (FileSplit)split;
				// For every split location
				for(String l : fs.getLocations()){
					// If this split is local to the server
					if(l.equals(server)){
						// Fix split location
						splits.add(new FileSplit(fs.getPath(), fs.getStart(), fs.getLength(), new String[] {server}));
						originalSplits.remove(split);
						replaced = true;
						break;
					}
				}
				if(replaced)
					break;
			}
			// If no local splits are found for this server
			if(!replaced){
				// Assign first available split to it 
				FileSplit fs = (FileSplit)splits.get(0);
				splits.add(new FileSplit(fs.getPath(), fs.getStart(), fs.getLength(), new String[] {server}));
				originalSplits.remove(0);				
			}
		}
		return splits;
	}

	private String[] getActiveServersList(JobContext context){

		String [] servers = null;
        try {
			JobClient jc = new JobClient((JobConf)context.getConfiguration()); 
			ClusterStatus status = jc.getClusterStatus(true);
			Collection<String> atc = status.getActiveTrackerNames();
			servers = new String[atc.size()];
			int s = 0;
			for(String serverInfo : atc){
				StringTokenizer st = new StringTokenizer(serverInfo, ":");
				String trackerName = st.nextToken();
			   	StringTokenizer st1 = new StringTokenizer(trackerName, "_");
			   	st1.nextToken();
			   	servers[s++] = st1.nextToken();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

        return servers;
	}
	
	private static int getNextServer(int current, int max){
		
		current++;
		if(current >= max)
			current = 0;
		return current;
	}
}