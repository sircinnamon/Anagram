import java.util.Arrays;
import java.util.HashMap;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Random;
import java.lang.StringBuilder;
/*
 * Riley Lahd - 27-08-16
 * Dailyprogrammer Intermediate 280 - Anagram maker
 * Uses an input word and a word list to generate valid anagrams using hash map
 * where any word that is an anagram a given word is stored with the same key
*/
public class FastAnagram
{
	public static final int DIC_SIZE = 17000; //roughly, for hash map creation
	public static final int MAX_ATTEMPTS = 300000; //roughly, for hash map creation

	//An anagram generator
	//Uses depth-first search through all possible arrangements
	//This class represents a node in the search tree
	private static String original;
	private static String letters;
	private static HashMap<String, String> dictionary;
	private static String current;
	private static String answer;
	public static void main(String[] args)
	{
		HashMap<String,String> dictionary = new HashMap<String,String>();
		File file = new File("");
		if(args.length == 2)
		{
			original = args[0].toLowerCase().replaceAll("[^a-z ]", "");
			letters = original.replaceAll(" ", "");
			file = new File(args[1]);
		}
		try{dictionary = genDictionary(file);}
		catch(IOException e){e.printStackTrace();System.exit(0);}

		int i = 0;
		int[] parts;
		while(i < MAX_ATTEMPTS && answer == null)
		{
			current = shuffle(letters);
			parts = partition(current);
			StringBuilder sb = new StringBuilder(current);
			int index = 0;
			for(int j = 0;j<parts.length-1;j++)
			{
				index = index + parts[j];
				sb.insert(index, " ");
			}
			current = sb.toString();
			//System.out.println(current);
			boolean solvable = true;
			for(String s : current.split(" "))
			{
				if(!hasAnagram(s,dictionary)){solvable = false;}
			}
			if(solvable){answer = solve(current,dictionary);}
			i++;
		}
		if(answer==null){System.out.println("None found.");}
		else{System.out.println(answer);}
	}

	private static boolean hasAnagram(String word,HashMap<String,String> dic)
	{
		String key = genWordKey(word);
		return !(dic.get(key)==null);
	}

	private static String shuffle(String word)
	{
		char[] chars = word.toCharArray();
		char swap;
		int j;
		Random r = new Random();
		for(int i = 0; i<chars.length-1;i++)
		{
			j = r.nextInt(chars.length);
			swap = chars[i];
			chars[i] = chars[j];
			chars[j] = swap;

		}
		return Arrays.toString(chars).replaceAll("[^a-z]","");
	}

	private static int[] partition(String word)
	{
		Random r = new Random();
		int total = word.length();
		String ints = ""; //this is terrible
		int split;
		while(total > 1)
		{
			split = r.nextInt(total)+1;
			ints = ints + "" + (char)split;
			total = total - split;
		}
		int[] splits = new int[ints.length()];
		for(int i = 0; i<splits.length;i++)
		{
			splits[i] = (int)ints.charAt(i);
		}
		return splits;
	}

	private static String solve(String words,HashMap<String,String> dic)
	{
		String answer = "";
		for(String s : words.split(" "))
		{
			answer = answer + chooseAnagram(s,dic) + " ";
		}
		return answer.trim();
	}

	private static String chooseAnagram(String word, HashMap<String,String> dic)
	{
		Random r = new Random();
		String[] anagrams = dic.get(genWordKey(word)).split(" ");
		String choice = anagrams[r.nextInt(anagrams.length)];
		return choice;
	}

	private static HashMap<String, String> genDictionary(File filename) throws IOException
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
		HashMap<String, String> dic = new HashMap<String, String>(DIC_SIZE);
		String line = br.readLine();
		String entry;
		String key;
		while(line != null)
		{
			line= line.toLowerCase();
			key = genWordKey(line);
			entry = dic.get(key);
			if(entry==null){dic.put(key,line);}
			else{dic.put(key,(entry+" "+line));}
			line = br.readLine();
		}
		return dic;
	}

	private static String genWordKey(String word)
	{
		char[] c = word.toCharArray();
		Arrays.sort(c);
		return Arrays.toString(c).replaceAll("[^a-z]","");
	}
}