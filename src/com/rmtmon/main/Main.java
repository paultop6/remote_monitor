package com.rmtmon.main;
import com.rmtmon.util.*;


public class Main {
	public static void main(String[] args)
	{
		System.out.println("stats.");
		CpuStats cpu_stats = new CpuStats();
		cpu_stats.get_load();
	}
}
