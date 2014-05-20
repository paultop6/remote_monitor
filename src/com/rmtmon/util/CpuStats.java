package com.rmtmon.util;

import java.util.regex.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Map;

public class CpuStats {

	private int prev_cpu_total;
	private int prev_total_used;
	float current_used;

	public CpuStats() {
		System.out.println("cpuStat");
		prev_cpu_total = 0;
  		prev_total_used = 0;

  		
	}

	public float get_load() {
		System.out.println("get_load");
		
		HashMap<String, ArrayList> cpu_map = new HashMap<String, ArrayList>();
		//ArrayList<Integer> total_cpu = new ArrayList<Integer>();

		ArrayList<String> cpu_ids = get_cpu_ids();

		System.out.println("Length : " + cpu_ids.size());

		for (int i = 0; i < cpu_ids.size(); i++){
			System.out.println("Cpu : " + cpu_ids.get(i));
		}

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
					System.out.println("Cpu_times : " + cpu_times);
					cpu_map.put(cpu_id, cpu_times);
					cpu_map.get(cpu_id).remove(cpu_id);
				}
			}
		}
		System.out.println("cpu_map size : " + cpu_map.size());

		int[] total_cpu = new int[10];

		for (Map.Entry<String, ArrayList> entry : cpu_map.entrySet()) {			
			for (int i = 0; i < entry.getValue().size(); i++) {
				int cpu_time_value = Integer.parseInt(entry.getValue().get(i).toString());
				total_cpu[i] = total_cpu[i] + cpu_time_value;
			}
			
		}

        System.out.println(Arrays.toString(total_cpu));

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
