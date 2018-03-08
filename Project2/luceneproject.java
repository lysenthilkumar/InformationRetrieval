import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.io.*;
import java.util.LinkedList;
import org.apache.lucene.index.Terms;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.index.MultiFields;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.PostingsEnum;
public class luceneproject 
{
	public static Map<String, LinkedList> hmi = new HashMap<String, LinkedList>();      //Hash map for inverted index used to populate with terms and docid's  
	public static void getPostings(String[] filewords, PrintWriter out_file)
	{
		LinkedList<Integer> result=new LinkedList<Integer>();	//Collects the resulting linked list of docid's
		for(int i=0;i<filewords.length;i++)					//for saving to file all the GetPostings output 
		{
			out_file.println("GetPostings");
			//out_file.write"GetPostings\n");
			result=hmi.get(filewords[i]);
			out_file.println(filewords[i]);			//Prints the query terms for Getpostings
			//out_file.writefilewords[i]+"\n");
			out_file.print("Postings list:");
			//out_file.print("Postings list: ");
			for(int j=0;j<result.size();j++)		//Parse and print the content for the docid's against the terms 
			{
				out_file.print(" "+result.get(j));
				//out_file.writeresult.get(j)+" ");
			}
		 	out_file.println();
		 	//out_file.write"\n");
		}
	}
	public static void taatand(String[] filewords, PrintWriter out_file) // TAAT-AND operation
	{
		LinkedList<Integer> a=new LinkedList<Integer>();		//Linked list that writes to itself
		LinkedList<Integer> b=new LinkedList<Integer>();		//Linked list used to compare 1 to n with a
		LinkedList<Integer> c=new LinkedList<Integer>();		//Temporary linked list to save the result back to 'a' 
		int index1=0,index2=0,index3=0,compare1 = 0,compare2=0,no_comparisons=0,flag=0;
		{
			{
				out_file.println("TaatAnd");
				//out_file.write"TaatAnd\n");
				for(int opt=0;opt<filewords.length;opt++)		//Writing to file the query terms
				{
					if(opt>0)
						out_file.print(" "+filewords[opt]);
					else 
						out_file.print(filewords[opt]);
					//out_file.writefilewords[opt]+" ");
				}
				a=hmi.get(filewords[0]);
				for(int k=1;k<filewords.length;k++)		//Loop for finding out the set of common docID's for all terms
				{
					flag=0;
					b=hmi.get(filewords[k]);
					while(index1<a.size()&&index2<b.size())	  //Loop runs till both the linked list are within it sizes
					{										  //when one ends there will not be any common terms anymore	
						compare1=a.get(index1);
						compare2=b.get(index2);
						no_comparisons++;
						if(compare1==compare2)				//If terms are equal both the pointers are moved ahead
						{
							flag++;
							c.addLast(compare1);
							index1++;
							index2++;
						}
						else if(compare1<compare2) 			//Increase the relative pointer for whichever value is lower
						{	
							index1++;
						}
						else
							index2++;
					}
					a=(LinkedList<Integer>)c.clone();
					c.clear();
					index1=0;
					index2=0;
				}
				if(flag!=0||filewords.length==1)		//Printing output
				{
					out_file.print("\nResults:");
					//out_file.write"\nResults: ");
					for(int york=0;york<a.size();york++)
					{
						out_file.print(" "+a.get(york));
						//out_file.writea.get(york)+" ");
						no_comparisons++;
					}
					out_file.println("\n"+"Number of documents in results: "+a.size());
					//out_file.write"\n"+"Number of documents in results: "+a.size());
					out_file.println("Number of comparisons: "+no_comparisons);
					//out_file.write"\n"+"Number of comparisons: "+no_comparisons);
				}
				else
				{
					out_file.print("\nResults: empty");
					//out_file.write"\nResults: empty");
						out_file.println("\n"+"Number of documents in results: 0");
					//out_file.write"\n"+"Number of documents in results: 0");
					out_file.println("Number of comparisons: "+no_comparisons);
					//out_file.write"Number of comparisons: "+no_comparisons+"\n");
				}
			}
		} 
	}
	public static void taator(String[] filewords, PrintWriter out_file)
	{
		LinkedList<Integer> a=new LinkedList<Integer>();		//Linked list that writes to itself
		LinkedList<Integer> b=new LinkedList<Integer>();		//Linked list used to compare 1 to n with a
		LinkedList<Integer> c=new LinkedList<Integer>();		//Temporary linked list to save the result back to 'a'
		int index1=0,index2=0,index3=0,compare1 = 0,compare2=0,no_comparisons=0,flag=0;
		{
			{
				out_file.println("TaatOr");	
				for(int opt=0;opt<filewords.length;opt++)		//Writing to file the query terms
				{
					if(opt>0)
						out_file.print(" "+filewords[opt]);
					else 
						out_file.print(filewords[opt]);
				}
				a=hmi.get(filewords[0]);
				for(int k=1;k<filewords.length;k++)				
				{
					b=hmi.get(filewords[k]);
					while(index1<a.size()&&index2<b.size())		//Loop for finding out the set of all docID's for all terms
					{
						compare1=a.get(index1);
						compare2=b.get(index2);
						//no_comparisons++;
						if(compare1==compare2)
						{
							//flag++;
							no_comparisons++;
							c.addLast(compare1);					//add once for common docID and increase both pointers
							index1++;
							index2++;
						}
						else if(compare1<compare2)					//lesser docID's pointer value is pushed ahead
						{
							//flag++;
							no_comparisons++;
							c.addLast(compare1);
							index1++;
						}
						else										//lesser is again added to the result and pointer is pushed ahead
						{
							//flag++;
							c.addLast(compare2);
							index2++;
						}
					}
					if(index1<a.size())							//add the rest of the in complete array into the result array
						while(index1<a.size())
						{
							c.addLast(a.get(index1));
							index1++;
						}
					else
						while(index2<b.size())
						{
							c.addLast(b.get(index2));
							index2++;
						}
					a=(LinkedList<Integer>)c.clone();
					c.clear();
					index1=0;
					index2=0;
				}
				if(no_comparisons!=0||filewords.length==1)				//write output to the file
				{
					out_file.print("\nResults:");
					for(int york=0;york<a.size();york++)
					{
						out_file.print(" "+a.get(york));
						no_comparisons++;
					}
					out_file.println("\n"+"Number of documents in results: "+a.size());
					out_file.println("Number of comparisons: "+no_comparisons);
				}
				else
				{
					out_file.print("Results: empty");
					/*for(int york=0;york<a.size();york++)
						out_file.print(" "+a.get(york)+" ");*/
					out_file.println("\n"+"Number of documents in results: 0");
					out_file.println("Number of comparisons: "+no_comparisons);
				}
			}
		} 
	}
	private static String String(LinkedList<Integer> a) {
		// TODO Auto-generated method stub
		return null;
	}
	public static void daatand(String[] filewords, PrintWriter out_file)
	{
		LinkedList<Integer> temp_list=new LinkedList<Integer>();
		LinkedList<Integer> interim_list=new LinkedList<Integer>();
		int min_size=99999,temp_int, counting,max_value=0,temp_value,tempint,index_copy=0,monitor=0;
		int inner_counting,flag_count=0,index_itr,york,cut_out=0;
		String small_listid = null;
		{
			{
				int no_comparisons = 0,check=0;
				Map<String, LinkedList> temp_hash = new HashMap<String, LinkedList>();
				LinkedList<Integer> result_list=new LinkedList<Integer>();
				out_file.println("DaatAnd");
				for(int opt=0;opt<filewords.length;opt++)		//prints the query terms
				{
					if(opt>0)
						out_file.print(" "+filewords[opt]);
					else 
						out_file.print(filewords[opt]);
				}
				out_file.print("\n");
				for(counting=0;counting<filewords.length;counting++)			//produces hashmap of just the query terms
					temp_hash.put(filewords[counting], hmi.get(filewords[counting]));
				int[] index_list=new int[temp_hash.size()];
				for(counting=0;counting<temp_hash.size();counting++)		//intializing pointer array to 0
					index_list[counting]=0;
				while(cut_out==0)
				{
					for(inner_counting=0;inner_counting<temp_hash.size();inner_counting++)	//run for all query terms
					{
						temp_list=temp_hash.get(filewords[inner_counting]);
						temp_value=temp_list.get(index_list[inner_counting]);
						if(max_value<temp_value)									//Generate maximum value
						{
							max_value=temp_value;
							index_copy=inner_counting;
						}
					}
					monitor=temp_hash.size();
					for(inner_counting=0,flag_count=0;inner_counting<monitor;inner_counting++)
					{
						{
						temp_list=temp_hash.get(filewords[inner_counting]);
						index_itr=index_list[inner_counting];
						temp_int=temp_list.get(index_list[inner_counting]);
						if(temp_int<max_value)								//move pointer if the posting list has a value smaller than the greatest value in the window
						{
							no_comparisons++;
							tempint=index_list[inner_counting];
							tempint++;
							index_list[inner_counting]=tempint;
						}
						else if((temp_int=temp_list.get(index_itr))==max_value)  //else if they are equal then increase the comparisons
						{
							if(index_copy!=inner_counting)
								no_comparisons++;
							flag_count++;
						}
						}
					}
					if(flag_count==temp_hash.size())		//if the flag value is equal to the no of query terms then copy the value into the third array
					{
						check++;
						interim_list=temp_hash.get(filewords[index_copy]);
						york=index_list[index_copy];
						result_list.addLast(interim_list.get(york));
						for(inner_counting=0;inner_counting<temp_hash.size();inner_counting++)		//push the pointer one space forward for all the query terms
						{
							temp_int=index_list[inner_counting];
							temp_int++;
							index_list[inner_counting]=temp_int;
							
						}
					}
					for(inner_counting=0;inner_counting<temp_hash.size();inner_counting++)   //see if the number of times the pointer is at null is equal to the no of query terms
						if((index_list[inner_counting])>=(temp_hash.get(filewords[inner_counting])).size())
							cut_out++;
				}
				if(check!=0)			//write output to the file
				{
					out_file.print("Results:");
					//System.out.println(result_list.size());
					for(york=0;york<result_list.size();york++)
					{
						out_file.print(" "+result_list.get(york));
						no_comparisons++;
					}
					out_file.println("\n"+"Number of documents in results: "+result_list.size());
					out_file.println("Number of comparisons: "+no_comparisons);
				}
				else
				{
					out_file.print("Results: empty");
					out_file.println("\n"+"Number of documents in results: 0");
					out_file.println("Number of comparisons: "+no_comparisons);
				}
			}
		}	
		/*catch(FileNotFoundException e)
		{
			out_file.println("Catch of daatand!");
		}*/
	}
	public static void daator(String[] filewords, PrintWriter out_file)
	{
		//out_file.println("Inside!");
		LinkedList<Integer> temp_list=new LinkedList<Integer>();
		//LinkedList<Integer> interim_list=new LinkedList<Integer>();
		int temp_int,min_value=99999,temp_value,tempint,monitor=0;
		int inner_counting,flag_count=0,york,cut_out=0,flag=0,index_copy=0;
		String small_listid = null;
		{ 
				int no_comparisons = 0,check=0;
				Map<String, LinkedList> temp_hash = new HashMap<String, LinkedList>();
				LinkedList<Integer> result_list=new LinkedList<Integer>();
				out_file.println("DaatOr");
				int[] index_list=new int[filewords.length];
				for(int opt=0;opt<filewords.length;opt++) 		//Populate Temporary Hashmap
					temp_hash.put(filewords[opt], hmi.get(filewords[opt]));
				for(int opt=0;opt<filewords.length;opt++)  		//print the query terms
				{
					if(opt>0)
						out_file.print(" "+filewords[opt]);
					else 
						out_file.print(filewords[opt]);
					index_list[opt]=0;
				}
				out_file.print("\n");
				while(cut_out<temp_hash.size())
				{
					min_value=99999;
					for(inner_counting=0,cut_out=0;inner_counting<temp_hash.size();inner_counting++)
					{
						if(index_list[inner_counting]<(temp_hash.get(filewords[inner_counting])).size())   //find the minimum value
						{
							temp_list=temp_hash.get(filewords[inner_counting]);
							temp_value=temp_list.get(index_list[inner_counting]);
							//no_comparisons++;
							if(min_value>temp_value)
							{
								flag++;
								min_value=temp_value;
								index_copy=inner_counting;
							}
						}
						else
							cut_out++;
					}
					result_list.addLast(min_value);
					index_list[index_copy]++;
					monitor=temp_hash.size();
					for(inner_counting=0,cut_out=0;inner_counting<monitor;inner_counting++)			//run loop for number of query terms 
					{
						if(index_list[inner_counting]<(temp_hash.get(filewords[inner_counting])).size())	//checks if all the pointers are within range
						{
							temp_list=temp_hash.get(filewords[inner_counting]);
							temp_int=temp_list.get(index_list[inner_counting]);
							if(temp_int==min_value)			//checks of the extracted value corresponds to minimum
							{
								index_list[inner_counting]++;	
								if(index_copy!=inner_counting)
									no_comparisons++;
							}
						}
						else
							cut_out++;
					}
				}
				if(flag!=0)						//write output to file
				{
					out_file.print("Results:");
					for(york=0;york<result_list.size();york++)
					{
						out_file.print(" "+result_list.get(york));
						no_comparisons++;
					}
					out_file.println("\n"+"Number of documents in results: "+result_list.size());
					out_file.println("Number of comparisons: "+no_comparisons);
				}
				else
				{
					out_file.print("Results: empty");
					out_file.println("\n"+"Number of documents in results: 0");
					out_file.println("Number of comparisons: "+no_comparisons);
				}
		}	
	}
	public static void main(String[] args)
	{
		try
		{
			//File query_output=new File("output.txt");
			PrintWriter out_file = new PrintWriter(new BufferedWriter(new FileWriter(args[1]))); //initialize the output file
			int flag=0,docID,count=1,uniquedoc=0;
			int tempdoc,size_count,k,innerflag=0,final_call=0,inner_final=0;
			LinkedList<Integer> list=new LinkedList<Integer>();//to append to hmi
			LinkedList<Integer> doclist= new LinkedList<Integer>();//to append to the hmf
			String keycontent; //to add to hmi printout the content of the inverted linked list
			String langarr[] = {"text_nl", "text_fr", "text_de", "text_ja", "text_ru", 
					"text_pt", "text_es", "text_it", "text_da", "text_no", 
					"text_sv"};							//run for all languages
			Path pathTool=Paths.get(args[0]);	//set path for lucene index
			try
			{
				IndexReader reader = DirectoryReader.open(FSDirectory.open(pathTool));
				//File logFile=new File("output1.txt");
				//PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(logFile)));	//the output file is being declared
				for(int i=0;i<11;i++)
				{
					Terms terms= MultiFields.getTerms(reader,langarr[i]);	//extract terms
					TermsEnum termsEnum = null;
					termsEnum = terms.iterator();
					termsEnum.seekCeil(new BytesRef());
					BytesRef oneterm = termsEnum.term();		//extract a single term
					while(oneterm != null)
					{
						keycontent=oneterm.utf8ToString();		//convert to string and save the value
						PostingsEnum postEnum=null;
						postEnum = termsEnum.postings(postEnum); 			//extract postings against a single term
						while (postEnum.nextDoc() != PostingsEnum.NO_MORE_DOCS)
						{
							docID=postEnum.docID();			//extract the docid
							list.add(docID);				//add to the list
						}
						hmi.put(keycontent, (LinkedList) list.clone());		//build the hashmap
						//out.write(keycontent+" "+hmi.get(keycontent)+"\n");
						//out_file.println("The *"+keycontent+"* index terms are: "+hmi.get(keycontent));
						list.clear();
						oneterm = termsEnum.next();
						flag++;
					}
				}
				reader.close();
			}
			catch(IOException e)
			{
				out_file.println("Input/Output Error");
			}
			try
			{
				Scanner scobj = new Scanner(new File(args[2]));		//use to extract the input for the queries
				String[] filewords = null,words;
				while(scobj.hasNextLine())
				{
					String fileline = scobj.nextLine();			//extract a line from the input file
					String bom_avoid="\uFEFF";					//BOM character to avoid set the string
					filewords=fileline.split(" ");				//break line into independent words
					for(int l=0;l<filewords.length;l++)	
						if(filewords[l].startsWith(bom_avoid))
						{
							filewords[l]=filewords[l].substring(1);		//check for the bom character and reassign the line
						}
					getPostings(filewords, out_file);		//pass the string array to all methods and call
					taatand(filewords, out_file);
					taator(filewords, out_file);
					daatand(filewords, out_file);
					daator(filewords, out_file);		
				}
				out_file.close();
			}
			catch(FileNotFoundException e)
			{
				out_file.println("Input File not found!");
			}
		}
		catch(IOException e)
		{
			System.out.println("Error! Catch Block code!");
		}
	}
}