package com.softdev.gl;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyDataStructureParser {

	private static final String TEST_DATA = "items:[id:PLAN INTERNET FIJO|quantity:1|producttype:Promotion|productcharacteristic:[name:service-type|value:Promotion,name:siebel-price-list|value:LP HOME],id:Servicio de Internet|quantity:1|producttype:Product|productcharacteristic:[name:service-type|value:Agrupador],id:Equipo Internet|quantity:1|producttype:Product]";
	private static final String TEST_DATA_2 = "p1:AAA|p2:BBB|p3:[p1:CC|p2:DDD|p3:[p1:EEE|p2:FFF|p3:[p1:GGG|p2:HHH,p1:III|p2:JJJ],p1:KKK|p2:LLL],p1:MMM|p2:NNN],p1:OOO|p2:PPP";
	private static final String TEST_DATA_3 = "p1:AAA|p2:BBB|p3:[p1:CC|p2:DDD|p3:[p1:EEE|p2:FFF|p3:[p1:GGG|p2:HHH,p1:III|p2:JJJ],p1:KKK|p2:LLL|p3:[p1:TTT|p2:UUU]],p1:MMM|p2:NNN],p1:OOO|p2:PPP|p3:[p1:QQQ|p2:RRR]";
	private static final String TEST_DATA_4 = "p1:AAA|p2:BBB|p3:[p1:CC|p2:DDD|p3:[p1:EEE|p2:FFF|p3:[p1:GGG|p2:HHH,p1:III|p2:JJJ],p1:KKK|p2:LLL|p3:[p1:TTT|p2:UUU]],p1:MMM|p2:NNN],p1:OOO|p2:PPP|p3:[p1:QQQ|p2:RRR],p1:WWW|p2:XXX";
	
	private static MyDataStructureParser parser = new MyDataStructureParser();
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Map<Integer,List<Integer[]>> beginEndSquareBracketStartEndIndexesPerLevel = parser.recordBeginEndSquareBracketStartEndIndexesPerLevel(TEST_DATA_4);
		List<Integer[]> beginEndObjectsIndexes = parser.getBeginEndObjectIndexPerLevel(beginEndSquareBracketStartEndIndexesPerLevel, 1, TEST_DATA_4);
		for(Integer[] pair : beginEndObjectsIndexes) {
			System.out.println(TEST_DATA_4.substring(pair[0], pair[1]));
		}
	}

	private Map<Integer,List<Integer[]>> recordBeginEndSquareBracketStartEndIndexesPerLevel(String rawData) {
		char [] charArray = rawData.toCharArray();
		Deque<Integer> stack = new ArrayDeque<Integer>();
		Map<Integer,List<Integer[]>> beginEndSquareBracketStartEndIndexesPerLevel = new HashMap<>();
		
		for(int i = 0; i < charArray.length; i++) {
			if(charArray[i] == '[') {
				stack.push(i);
			}else if (charArray[i] == ']') {
				//squareBracketsBeginEndIndexes.add(new Integer [] {stack.pop(),i});
				if(beginEndSquareBracketStartEndIndexesPerLevel.containsKey(stack.size())) {
					beginEndSquareBracketStartEndIndexesPerLevel.get(stack.size()).add(new Integer [] {stack.pop(),i});
				}else {
					int newLevel = stack.size();
					List<Integer[]> newLevelList = new ArrayList<>();
					newLevelList.add(new Integer [] {stack.pop(),i});					
					beginEndSquareBracketStartEndIndexesPerLevel.put(newLevel, newLevelList);					
				}				
			}
		}
		return beginEndSquareBracketStartEndIndexesPerLevel;
	}
	
	private List<Integer[]> getBeginEndObjectIndexPerLevel(Map<Integer,List<Integer[]>> beginEndSquareBracketStartEndIndexesPerLevel, int level, String rawData){
		List<Integer[]> beginEndSquareBracketStartEndIndexes = beginEndSquareBracketStartEndIndexesPerLevel.get(level);
		int beginObjectIndex = 0;
		List<Integer[]> beginEndObjectIndexPerLevel = new ArrayList<>();
		for(int i = 0; i < beginEndSquareBracketStartEndIndexes.size(); i++) {			
			int subStringBeginIndex = beginEndSquareBracketStartEndIndexes.get(i)[1];
			int subStringEndIndex = 0;
			if(i == beginEndSquareBracketStartEndIndexes.size() - 1) {			
				subStringEndIndex = rawData.length() - 1;
			}else {
				subStringEndIndex = beginEndSquareBracketStartEndIndexes.get(i + 1)[0];
			}
			char [] charArray = rawData.substring(subStringBeginIndex, subStringEndIndex).toCharArray();
			for(int i1 = 0; i1 < charArray.length; i1++) {
				if(charArray[i1] == ',') {
					int endObjectIndex = subStringBeginIndex + i1;
					beginEndObjectIndexPerLevel.add(new Integer [] {beginObjectIndex, endObjectIndex});
					System.out.println(rawData.substring(beginObjectIndex, endObjectIndex));
					System.out.println(rawData.substring(beginObjectIndex, endObjectIndex).length());
					beginObjectIndex = endObjectIndex + 1;
				}
			}
		}
		if(beginObjectIndex < rawData.length()) {
			beginEndObjectIndexPerLevel.add(new Integer [] {beginObjectIndex, rawData.length() - 1});
		}
		
		return beginEndObjectIndexPerLevel;
	}
	
}
