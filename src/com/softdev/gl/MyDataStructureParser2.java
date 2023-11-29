package com.softdev.gl;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyDataStructureParser2 {

	private static final String TEST_DATA = "p1:AAA|p2:BBB|p3:[p1:CC|p2:DDD|p3:[p1:EEE|p2:FFF|p3:[p1:GGG|p2:HHH,p1:III|p2:JJJ],p1:KKK|p2:LLL|p3:[p1:TTT|p2:UUU]],p1:MMM|p2:NNN],p1:OOO|p2:PPP|p3:[p1:QQQ|p2:RRR],p1:WWW|p2:XXX";
	
	private static final MyDataStructureParser2 parser = new MyDataStructureParser2();
	
	public static void main(String[] args) {
		parser.testGetObjectIndexes();
	}
	
	private List<Map<String,Object>> getObjectList(String rawData, int beginIndex, int endIndex) {
		List<Integer[]> objectsIndexes = getObjectIndexes(rawData, beginIndex, endIndex);
		List<Map<String,Object>> objectList = new ArrayList<>();
		for(Integer[] beginEndObjectIndex : objectsIndexes) {
			objectList.add(getObject(rawData, beginEndObjectIndex[0], beginEndObjectIndex[1]));
		}
		return objectList;
	}
	
	private Map<String,Object> getObject(String rawData, int beginIndex, int endIndex){
		Map<String,Object> object = new HashMap<>();
		char[] charArray = rawData.substring(beginIndex, endIndex).toCharArray();
		String propertyKey = "";
		String propertyValue = "";
		boolean keyInProgress = true;
		int i = 0;
		while(i < charArray.length) {			
			if(charArray[i] == ':') {
				propertyKey = "";
				keyInProgress = false;
			}else if(charArray[i] == '|') {
				propertyValue = "";
				keyInProgress = true;
				object.put(propertyKey, propertyValue);
			}else if(charArray[i] == '[') {
				int listContentBeginIndex = beginIndex + i + 1;
				int endListIndex = getEndListIndex(rawData, listContentBeginIndex - 1);
				List<Map<String,Object>> objectList = getObjectList(rawData, listContentBeginIndex, endListIndex - 1);
				object.put(propertyKey, objectList);
				i = endListIndex;
			}else if (keyInProgress) {
				propertyKey+=charArray[i];
			}else {
				propertyValue+=charArray[i];
			}
			i++;
		}
		return object;
	}
	
	private int getEndListIndex(String rawData, int beginIndex) {
		char[] charArray = rawData.toCharArray();
		Deque<Integer> stack = new ArrayDeque<Integer>();
		int i = beginIndex;
		while(i < charArray.length) {
			if(charArray[i] == '[') {
				stack.push(i);				
			}else if(charArray[i] == ']') {				
				stack.pop();
			}else if(stack.size() == 0) {
				break;
			}
			i++;
		}
		return i;		
	}
	
	/** ################################# ################################# */
	/** ################################# ################################# */
	
	private void testGetObjectIndexes() {
		List<Integer[]> objectIndexes = parser.getObjectIndexes(TEST_DATA, 0, TEST_DATA.length() - 1);
		for(Integer[] objectBeginEndIndexes : objectIndexes) {
			System.out.println(objectBeginEndIndexes[0] + ", " +  objectBeginEndIndexes[1]);
			System.out.println(TEST_DATA.substring(objectBeginEndIndexes[0], objectBeginEndIndexes[1]));
		}
	}
	
	private List<Integer[]> getObjectIndexes(String rawData, int beginSearchIndex, int endSearchIndex){
		Deque<Integer> stack = new ArrayDeque<Integer>();
		String subString = rawData.substring(beginSearchIndex, endSearchIndex);
		char[] charArray = subString.toCharArray();
		List<Integer[]> objectListBeginEndIndexes = new ArrayList<>();
		int beginObjectIndex = beginSearchIndex;
		boolean checkCommaCharacter = true;
		int currentObjectCharactersLength = 0;
		for(int i = 0; i < charArray.length; i++) {
			currentObjectCharactersLength++;
			if(charArray[i] == '[') {
				stack.push(i);
				checkCommaCharacter = false;
			}else if(charArray[i] == ']') {				
				stack.pop();
				if(stack.size() == 0){
					checkCommaCharacter = true;
				}
			}else if(checkCommaCharacter && charArray[i] == ',') {				
				objectListBeginEndIndexes.add(new Integer[] {beginObjectIndex, beginObjectIndex + currentObjectCharactersLength - 1});
				beginObjectIndex = beginObjectIndex + currentObjectCharactersLength;
				currentObjectCharactersLength = 0;
			}			
		}		
		objectListBeginEndIndexes.add(new Integer[] {beginObjectIndex, beginObjectIndex + currentObjectCharactersLength});
		return objectListBeginEndIndexes;
	}
}
