import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.UnaryOperator;

public class PrettifyDFA {
	DFA unprettyDFA;
	String[][] distinct;
	List<List<String>> equivalents;
	boolean inFinal1 = false;
	boolean inFinal2 = false;
	
	public PrettifyDFA(DFA uglyDFA) {
		unprettyDFA = uglyDFA;
		distinct = new String[unprettyDFA.Q.size()+1][unprettyDFA.Q.size()+1];
		
		//initialize distinct table
		for(int row = 1; row < distinct.length; row++){
			for(int col = 1; col < distinct.length; col++){
				distinct[row][col] = "0";
			}
		}
		for(int row = 1; row < distinct.length; row++){
			distinct[row][0] = unprettyDFA.Q.get(row-1).get(0);
		}
		for(int col = 1; col < distinct.length; col++){
			distinct[0][col] = unprettyDFA.Q.get(col-1).get(0);
		}
		distinct[0][0] = "0";
		
	}
	
	public void Run(){
		makePretty();
		System.out.println();
		/*for(int row = 0; row < distinct.length; row++){
			for(int col = 0; col < distinct.length; col++){
				System.out.print(distinct[row][col] + " ");
			}
			System.out.println();
		}*/
		equivalents = IsEquivalence();
		MergeEquivs();
	}
	
	
	 //C:\Users\Asus\Desktop\test.txt
	private void makePretty(){

		//Step 1 of prettifying algorithm
			for(int row = 0; row < unprettyDFA.Q.size(); row++){
				for(int col = 0; col < unprettyDFA.Q.size(); col++){
					for(int i = 0; i < unprettyDFA.Final.size(); i ++){
						if(unprettyDFA.Final.get(i).contains(unprettyDFA.Q.get(col).get(0))){
							inFinal1 = true;
						}
						if(unprettyDFA.Final.get(i).contains(unprettyDFA.Q.get(row).get(0))){
							inFinal2 = true;
						}
					}
					
					if(inFinal1 ^ inFinal2){
						distinct[row+1][col+1] = "1";
					}
					inFinal1 = false;
					inFinal2 = false;
				}
			}
				
			/*for(int row = 0; row < distinct.length; row++){
				for(int col = 0; col < distinct.length; col++){
					System.out.print(distinct[row][col] + " ");
				}
				System.out.println();
			}*/
		
		//Step 2 of prettifying algorithm
		boolean change = false;
		boolean changeEnd = true;
		while(changeEnd == true){
			for(int row = 1; row < distinct.length; row++){
				for(int col = 1; col < distinct.length; col++){
					for(int i = 0; i < unprettyDFA.Alphabet.size(); i++){
						if(row != col){ //don't check same state against itself
							if(distinct[row][col] == "0"){
								boolean isDistinct = RunTransition(row, col, unprettyDFA.Alphabet.get(i));
								if(isDistinct == true){
									distinct[row][col] = "1";
									change = true;
								}
							}
						}
					}
				}
			}
			changeEnd = change;
			change = false;
		}
	}


	private boolean RunTransition(int row, int col, String alpha) {
		String state1 = "";
		String state2 = "";
		int found1 = -1;
		int found2 = -1;
		
		//Run Transition Function on both states
		for(int j = 0; j < unprettyDFA.TF.size(); j++){
			if(unprettyDFA.TF.get(j).a.get(0).equals(distinct[row][0])){
				if(unprettyDFA.TF.get(j).b.equals(alpha)){
					state1 = unprettyDFA.TF.get(j).c.get(0);
					break;
				}
			}
		}
		//This breaks if there are alphabet symbols that aren't used in the transition-function
		for(int i = 0; i < unprettyDFA.TF.size(); i++){
			if(unprettyDFA.TF.get(i).a.get(0).equals(distinct[0][col])){
				if(unprettyDFA.TF.get(i).b.equals(alpha)){
					state2 = unprettyDFA.TF.get(i).c.get(0);
					break;
				}
			}
		}
		
		//Find state1 and state2's row and column number to plug into the distinct table
		for(int i = 0; i < distinct.length; i++){
			for(int j = 0; j < distinct.length; j++){
				if(distinct[i][0].trim().equals(state1.trim())){
					if(distinct[0][j].equals(state2)){
						found1 = i;
						found2 = j;
						break;
					}
				}
			}
		}
		
		//check output from transition function in distinct table
		if(found1 != -1 && found2 != -1){  
			if(distinct[found1][found2].equals("1")){
				return true;
			}
			else{
				return false;
			}
		}else{
			return false;   //means state doesn't have a transition with that alphabet symbol
		}
		
	}
	
	
	private List<List<String>> IsEquivalence(){
		//look at only bottom diagonal of distinct table
		List<List<String>> equivs = new ArrayList<List<String>>();
		int i = 1;
		String eq1= "";
		String eq2= "";
		for(int col = 1; col < distinct.length; col++){
			for(int row = distinct.length-1; row > i; row--){
				if(distinct[row][col].equals("0")){
					
					//found an equivalent pair
					eq1 = distinct[0][col];
					eq2 = distinct[row][0];
					if((!equivs.contains(eq1)) || (!equivs.contains(eq2))){
						List<String> addMe = new ArrayList<String>();
						addMe.add(eq1);
						addMe.add(eq2);
						equivs.add(addMe);
					}
				}
			}
			i++;
		}
		
		return ElimDupEquiv(equivs);
	}
	
	//Merge any equivalent states
	private List<List<String>> ElimDupEquiv(List<List<String>> simplifyMe){
		List<List<String>> simplified = new ArrayList<List<String>>();
		boolean flag1 = true;
		boolean flag2 = true;
		int position1 = 0;
		int position2 = 0;
		
		for(int i = 0; i < simplifyMe.size(); i++){
			for(int j = 0; j < simplifyMe.get(i).size(); j++){
				for(int k = 0; k < simplified.size(); k++){
					
					//check to see if state already in simplified
					if(simplified.get(k).contains(simplifyMe.get(i).get(0))){
						flag1 = false;
						position1 = k;
					}
					if(simplified.get(k).contains(simplifyMe.get(i).get(1))){
						flag2 = false;
						position2 = k;
					}
				}
				
				//add appropriate elements
				if(flag1 == false){
					simplified.get(position1).add(simplifyMe.get(i).get(1));
				}
				if(flag2 == false){
					simplified.get(position2).add(simplifyMe.get(i).get(0));
				}
				if(flag1 == true & flag2 == true){
					List<String> temp = new ArrayList<String>();
					temp.add(simplifyMe.get(i).get(1));
					temp.add(simplifyMe.get(i).get(0));
					simplified.add(temp);
				}
				flag1 = true;
				flag2 = true;
			}
		}
		
		for(int i = 0; i < simplified.size(); i++){
			List<String> temp = new ArrayList<String>(RemoveDups(simplified.get(i)));
			simplified.get(i).clear();
			simplified.get(i).addAll(temp);
		}
		
		return simplified;
	}
	
	//Remove duplicates in equivalent states
	private List<String> RemoveDups(List<String> cleanMe){
		Set<String> hs = new HashSet<>();
		hs.addAll(cleanMe);
		cleanMe.clear();
		cleanMe.addAll(hs);
		return cleanMe;
	}
	
	private void MergeEquivs(){
		List<String> removeMe = new ArrayList<String>();
		for(List<String> LS : equivalents){
			for(String s : LS){
				removeMe.add(s);
			}
		}
		
		//Remove all equivalent state singletons in Q and replace with the equivalent set
		for(int i = 0; i < unprettyDFA.Q.size(); i++){
			unprettyDFA.Q.get(i).removeAll(removeMe);
		}
		
		for(int i = 0; i < unprettyDFA.Q.size(); i++){
			if(unprettyDFA.Q.get(i).size() == 0){
				unprettyDFA.Q.remove(i);
				i--;
			}
		}
		
		for(List<String> str : equivalents){
			unprettyDFA.Q.add(str);
		}
		
		//Remove all equivalent state singletons in Final and replace with the equivalent set
		for(int i = 0; i < unprettyDFA.Final.size(); i++){
			for(int j = 0; j < equivalents.size(); j++){
				if(unprettyDFA.Final.get(i).size() == 1){
					if(equivalents.get(j).contains(unprettyDFA.Final.get(i).get(0))){
						unprettyDFA.Final.remove(unprettyDFA.Final.get(i));
						unprettyDFA.Final.add(equivalents.get(j));
						i--;
						break;
					}
				}
			}
		}
		
		
		//remove any duplicates from Final
			Set<List<String>> hs = new HashSet<>();
			hs.addAll(unprettyDFA.Final);
			unprettyDFA.Final.clear();
			unprettyDFA.Final.addAll(hs);
		
		
		//Remove all equivalent state singletons in Transition Function and replace with the equivalent set
		for(int i = 0; i < unprettyDFA.TF.size(); i++){
			for(int j = 0; j < equivalents.size(); j++){
				if(equivalents.get(j).contains(unprettyDFA.TF.get(i).a.get(0))){
					List<String> addMe = new ArrayList<String>();
					for(String LS : equivalents.get(j)){
						addMe.add(LS);
					}
					unprettyDFA.TF.get(i).a = new ArrayList<String>(addMe);
				}
				if(equivalents.get(j).contains(unprettyDFA.TF.get(i).c.get(0))){
					List<String> addMe = new ArrayList<String>();
					for(String LS : equivalents.get(j)){
						addMe.add(LS);
					}
					unprettyDFA.TF.get(i).c = new ArrayList<String>(addMe);
				}
			}
		}
		
		//remove any duplicates from Transition-Function
		TrimTransFunc();
	}
	
	private void TrimTransFunc(){
		int count = 0;
		for(int i = 0; i < unprettyDFA.TF.size(); i++){
			for(int j = 0; j < unprettyDFA.TF.size(); j++){
				if(unprettyDFA.TF.get(i).b.trim().equals(unprettyDFA.TF.get(j).b.trim())){
					if(unprettyDFA.TF.get(i).a.equals(unprettyDFA.TF.get(j).a)){
						if(unprettyDFA.TF.get(i).c.equals(unprettyDFA.TF.get(j).c)){
							count++;
							if(count > 1){
								unprettyDFA.TF.remove(i);
								count--;
								j--;
							}
						}
					}
				}
			}
			count = 0;
		}
		for(int i = 0; i < unprettyDFA.TF.size(); i++){
			Set<ThreeTuple> hs = new HashSet<>();
			hs.addAll(unprettyDFA.TF);
			unprettyDFA.TF.clear();
			unprettyDFA.TF.addAll(hs);
		}
	}
	
}
