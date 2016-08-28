import java.util.Arrays;
import java.util.HashMap;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.IOException;
/*
 * Riley Lahd - 27-08-16
 * Dailyprogrammer Intermediate 280 - Anagram maker
 * Uses an input word and a word list to generate valid anagrams using brute force
 *
*/
public class Anagram
{
	public static final int DIC_SIZE = 17000; //roughly, for hash map creation
	//An anagram generator
	//Uses depth-first search through all possible arrangements
	//This class represents a node in the search tree
	String original;
	String letters;
	HashMap<String, String> dictionary;
	String current;
	boolean listMode = false;
	String answer;
	public static void main(String[] args)
	{
		String inputOriginal = "";
		HashMap<String,String> inputDictionary = new HashMap<String,String>();
		File file = new File("");
		if(args.length == 2)
		{
			inputOriginal = args[0].toLowerCase().replaceAll("[^a-z ]", "");
			file = new File(args[1]);
		}
		//TODO: add case where listMode can be set with argument

		try{inputDictionary = genDictionary(file);}
		catch(IOException e){e.printStackTrace();System.exit(0);}
		Anagram root = new Anagram(inputOriginal, "", inputOriginal.replaceAll(" ",""), inputDictionary, false);
		if(root.answer != null){System.out.print(root.answer);}
		else{System.out.println("No anagram found!");}
	}

	public Anagram(String orig, String curr, String lett, HashMap<String,String> dic, boolean list)
	{
		original = orig;
		current = curr;
		letters = lett;
		dictionary = dic;
		listMode = list;

		//System.out.println("@"+current);
		if(letters.length()==0)
		{
			validateFinal();
		}
		else
		{
			generateChildren();
		}
	}

	private void validateFinal()
	{
		//check if everything after last space is a valid word
		//if so answer = current
		//else answer = ""
		if(isWord(getLastWord()))
		{
			if(!current.equals(original)){answer = current.trim() + "\n";}
		}
	}

	private void generateChildren()
	{
		//generate children and continue searching
		//if the last word is a word, try with a space also
		String lettersTried = "";
		for(int i = 0; i < letters.length(); i++)
		{
			char letter = letters.charAt(i);
			if(!lettersTried.contains(letter+""))
			{
				lettersTried = lettersTried + "" + letter;
				Anagram child = new Anagram(original, current+letter, letters.replaceFirst(letter+"",""), dictionary, listMode);
				if(child.answer != null)
				{
					if(listMode)
					{
						this.answer = this.answer + child.answer;
					}
					else
					{
						this.answer = child.answer;
						break;
					}
				}
			}
		}
		if(isWord(getLastWord()) && current.charAt(current.length()-1)!=' ')
		{
			if(listMode)
			{
				Anagram child = new Anagram(original, current+" ", letters, dictionary, listMode);
				if(child.answer != null)
				{
					this.answer = this.answer + child.answer;
				}
			}
			else if(this.answer == null)
			{
				Anagram child = new Anagram(original, current+" ", letters, dictionary, listMode);
				if(child.answer != null)
				{
					this.answer = child.answer;
				}
			}
		}
	}

	private String getLastWord()
	{
		if(current.split(" ").length==0){return "";}
		return current.split(" ")[current.split(" ").length-1];
	}

	private boolean isWord(String word)
	{
		return !(dictionary.get(word) == null);
	}

	private static HashMap<String, String> genDictionary(File filename) throws IOException
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
		HashMap<String, String> dic = new HashMap<String, String>(DIC_SIZE);
		String line = br.readLine();
		while(line != null)
		{
			line= line.toLowerCase();
			dic.put(line, line);
			line = br.readLine();
		}
		return dic;
	}
}