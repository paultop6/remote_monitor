package com.rmtmon.util;

import java.util.regex.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Map;

public class CpuStats {

	private int prev_used;
	private int prev_total;
	float current_used;

	public CpuStats() {
		System.out.println("cpuStat");
  		prev_used = 0;
  		prev_total = 0;
  		
	}

	public float get_load() {
		System.out.println("get_load");
		float current_usage = 0;
		int used,total;
		
		HashMap<String, ArrayList> cpu_map = new HashMap<String, ArrayList>();

		ArrayList<String> cpu_ids = get_cpu_ids();

		ArrayList<String> proc_stat = new ArrayList<String>();
		try {
			Scanner s = new Scanner(new File("/proc/stat")).useDelimiter("\n");
			while (s.hasNext()) {
				proc_stat.add(s.next());
			}
		s.close();
		} catch (FileNotFoundException e) {
			System.out.println("FileNotFoundException /proc/stat");
			return 0;
		}

		for (String line : proc_stat) {
			for (String cpu_id : cpu_ids) {
				if (line.startsWith(cpu_id)) {
					ArrayList<String> cpu_times = new ArrayList<String>(Arrays.asList(line.split(" ")));
					cpu_map.put(cpu_id, cpu_times);
					cpu_map.get(cpu_id).remove(cpu_id);
				}
			}
		}
		int[] total_cpu = new int[10];

		for (Map.Entry<String, ArrayList> entry : cpu_map.entrySet()) {			
			for (int i = 0; i < entry.getValue().size(); i++) {
				//System.out.println("String before : " + entry.getValue().get(i).toString());
				int cpu_time_value = Integer.parseInt(entry.getValue().get(i).toString());
				//System.out.println("Float after : " + cpu_time_value);
				total_cpu[i] = total_cpu[i] + cpu_time_value;
			}
			
		}
		
		// total_cpu[0] = cpu
		// total_cpu[1] = nice
		// total_cpu[2] = system
		// total_cpu[3] = idle
		
		if (prev_used == 0) {
			System.out.println("First loop, continue");
			prev_used = total_cpu[0] + total_cpu[1] + total_cpu[2];
			prev_total = total_cpu[0] + total_cpu[1] + total_cpu[2] + total_cpu[3];
			return 0;
		}
		
		used = total_cpu[0] + total_cpu[1] + total_cpu[2];
		total = total_cpu[0] + total_cpu[1] + total_cpu[2] + total_cpu[3];
		
		current_usage = ((float)(used - prev_used) / (float)(total - prev_total));
		
		prev_used = used;
		prev_total = total;  		
  		
  		System.out.println("Current Usage : " + current_usage);
		System.out.println("Current Percentage : " + current_usage * 100);
		
		return 0;

	}

	public float get_pid_cpu_used(String pid) {
		System.out.println("get_pid_cpu_used");
		return 0;

	}

	public float get_cpu_total() {
		System.out.println("get_cpu_total");
		return 0;

	}

	private ArrayList<String> get_cpu_ids() {
		ArrayList<String> ar = new ArrayList<String>();
		BufferedReader r = null;
		Pattern str_pattern = Pattern.compile("processor");
		try {
			r = new BufferedReader(new FileReader("/proc/cpuinfo"));
		} catch (FileNotFoundException e) {
			System.out.println("File not found /proc/cpuinfo");
			return null;
		}

		try {
			String line;
			while ((line = r.readLine()) != null) {
				Matcher matcher = str_pattern.matcher(line);
				while (matcher.find()) {
					String[] str_split = line.split(":");
					if (str_split[1] != null) {
						ar.add("cpu" + str_split[1].replaceAll("\\s+",""));
					}
				}
			}
			r.close();
		} catch (IOException e) {
			System.out.println("IOException /proc/cpuinfo");
			return null;
		}
		return ar;
	} 

}
