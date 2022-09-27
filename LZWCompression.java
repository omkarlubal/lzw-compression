import java.util.*;
import java.util.stream.Collectors;
import java.io.*;

class LZCore {
	
	int dictSize = 256;
	
	
	List<Integer> encode(String s) {
		Map <String, Integer> dictionary = new HashMap<>();
		
		for (int i = 0; i < dictSize; i++)
			dictionary.put(String.valueOf((char) i), i);
		
		String currentChars = "";
		List<Integer> result = new ArrayList<>();
		for (char character : s.toCharArray()) {
			String charsToAdd = currentChars + character;
			if (dictionary.containsKey(charsToAdd)) {
				// already have an entry
				// mup
				currentChars = charsToAdd;
			} else {
				result.add(dictionary.get(currentChars));
				dictionary.put(charsToAdd, dictSize++);
				currentChars = String.valueOf(character);
			}
		}
		
		if (!currentChars.isEmpty()) {
			result.add(dictionary.get(currentChars));
		}
		
		System.out.println("Encoded: " + result.stream().map(String::valueOf).collect(Collectors.joining("")));
		
		return result;
	}
	
	String decode(List<Integer> encodedText) {
		Map <Integer, String> dictionary = new HashMap<>();
		
		for (int i = 0; i < dictSize; i++)
			dictionary.put(i, String.valueOf((char) i));
		
		String characters = String.valueOf((char) encodedText.remove(0).intValue());
		StringBuilder result = new StringBuilder(characters);
		for (int code : encodedText) {
			String entry = dictionary.containsKey(code) ? 
						dictionary.get(code) : characters + characters.charAt(0);
			result.append(entry);
			dictionary.put(dictSize++, characters + entry.charAt(0));
			characters = entry;
		}
		
		return result.toString();
	}
}



class LZWCompression {
	
	

	public static void main(String args[]) {
		long startTime = System.nanoTime();
		String s = "";
		
		try {
		  File myObj = new File("input_long.txt");
		  Scanner myReader = new Scanner(myObj);
		  while (myReader.hasNextLine()) {
			String data = myReader.nextLine();
			s += data;
		  }
		  myReader.close();
		} catch (FileNotFoundException e) {
		  System.out.println("An error occurred.");
		  e.printStackTrace();
		}		
					
		LZCore lzcore = new LZCore();
		System.out.println(s);
		List<Integer> encoded = lzcore.encode(s);
		System.out.println(lzcore.decode(encoded));
		
		try {
			FileWriter myWriter = new FileWriter("output_final.txt");
			myWriter.write(encoded.stream().map(String::valueOf).collect(Collectors.joining("")));
			myWriter.close();
			System.out.println("Successfully wrote to the file.");
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
		long endTime   = System.nanoTime();
		
		long totalTime = endTime - startTime;
		System.out.println(totalTime/1000000);
	}

}