import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class BadToTheBoneParser<T>{
	public DFA ParseFile(String path)
	{
		BufferedReader buffReader;
		boolean statesFlag = false;
		boolean alphaFlag = false;
		boolean transFlag = false;
		boolean startFlag = false;
		boolean finalFlag = false;
		DFA nonMin = new DFA();
		
	    try
        {                           
        	FileReader reader = new FileReader(path);
        	buffReader = new BufferedReader(reader);
        	String text; 
        	
        	while((text = buffReader.readLine()) != null)
        	{	
        		String[] tokens = text.split("[, \\(\\)]+");
        		
        		for(int i = 1; i < tokens.length; i++){
        			if((!tokens[i].equals("")) && (!tokens[i].equals(" "))){
		        		if(tokens[1].trim().equals("states") && !alphaFlag && !transFlag && !startFlag && !finalFlag){
		        			statesFlag = true;
		        			if(i >1){
		        	        	List<String> addMe = new ArrayList<String>();
		        				addMe.add(tokens[i]);
		        				nonMin.Q.add(addMe);
		        			}
		        		}
		        		else if(tokens[1].equals("alpha") && statesFlag && !transFlag && !startFlag && !finalFlag){
		        			alphaFlag = true;
		        			if(i > 1){
		        				nonMin.Alphabet.add(tokens[i]);
		        			}
	        			}
		        		else if(tokens[1].equals("trans-func")&& alphaFlag && statesFlag && !startFlag && !finalFlag){
		        			transFlag = true;
		        			
		        			//Check to make sure there are the correct number of inputs to create ThreeTuples
		        			if((tokens.length-2)%3 != 0 || tokens.length < 5){
		        				throw new Exception("Malformed input. Please correct and try again.");
		        			}
		        			if(i > 1 && i < tokens.length-2){
		        				List<String> AddAs = new ArrayList<String>();
		        	        	List<String> AddCs = new ArrayList<String>();
		        				AddAs.add(tokens[i]);
		        				String b = tokens[i+1];
		        				AddCs.add(tokens[i+2]);
		        				nonMin.TF.add(new ThreeTuple(AddAs, b, AddCs));
			        			i += 2;
	        				}
		        		}
		        		else if(tokens[1].equals("start") && statesFlag && alphaFlag && transFlag && !finalFlag){
		        			startFlag = true;
		        			if(i > 1){
		        				String temp = (tokens[i]);
		        				nonMin.Qo.add(temp);
		        			}
		        		}
		        		else if(tokens[1].equals("final") && statesFlag && alphaFlag && transFlag && startFlag){
		        			finalFlag = true;
		        			if(i > 1){
		        	        	List<String> addMe = new ArrayList<String>();
		        				addMe.add(tokens[i]);
		        				nonMin.Final.add(addMe);
		        			}
		        		}else{
		        			throw new Exception("Malformed input. Please correct and try again.");
		        		}
        			}
        		}
        	}
        	
        	if(!CheckStartFormat(nonMin.Qo, nonMin.Q) || !CheckFinalFormat(nonMin.Final, nonMin.Q) || !CheckTransFuncFormat(nonMin.TF, nonMin.Q, nonMin.Alphabet)){
        		throw new Exception("I'm sorry Dave I can't do that. There was malformed input.  Please correct it and try again.");
        	}
        	
        	buffReader.close();
        	reader.close();
    		return nonMin;
        }
        catch (Exception ex)
        {
        	System.out.println(ex.getMessage());
        }               
        
	    
	    return null;
	}

	private boolean CheckStartFormat(List<String> qo, List<List<String>> q) {
		boolean flag = false;
		if(qo.size() == 1){
			for(int i = 0; i < q.size(); i++){
				if(q.get(i).contains(qo.get(0))){
					flag = true;
				}
			}
		}
		return flag;
	}

	private boolean CheckFinalFormat(List<List<String>> final1, List<List<String>> q) {
		boolean flag1 = false;
		boolean flag2 = false;
		
		for(List<String> FS : final1){
			for(String S : FS){
				for(List<String> QS : q){
					if(QS.contains(S)){
						flag1 = true;
						break;
					}
				}
				flag2 = flag1;
				if(flag1 == false){
					return false;
				}
				flag1 = false;
			}
		}
		
		//for no final states
		if(final1.size() == 0){
			flag2 = true;
		}
		return flag2;
	}

	private boolean CheckTransFuncFormat(List<ThreeTuple> tF, List<List<String>> q, List<String> alpha) {
		boolean flag1 = false;
		
		//Check all a values in ThreeTuples are in Q
		for(int i = 0; i < tF.size(); i++){
			for(int j = 0; j < q.size(); j++){
				if(q.get(j).contains(tF.get(i).a.get(0))){
					flag1 = true;
					break;
				}
			}
			if(flag1 == true){
				flag1 = false;
			}else{
				return false;
			}
		}
		
		//Check all b values in ThreeTuples are in Q
		for(int i = 0; i < tF.size(); i++){
			for(int j = 0; j < q.size(); j++){
				if(alpha.contains(tF.get(i).b)){
					flag1 = true;
					break;
				}
				
			}
			if(flag1 == true){
				flag1 = false;
			}else{
				return false;
			}
		}
		
		//Check all c values in ThreeTuples are in Q
		for(int i = 0; i < tF.size(); i++){
			for(int j = 0; j < q.size(); j++){
				if(q.get(j).contains(tF.get(i).c.get(0))){
					flag1 = true;
					break;
				}
			}
			if(flag1 == true){
				flag1 = false;
			}else{
				return false;
			}
		}
		
		
		return true;
	}
	   
}