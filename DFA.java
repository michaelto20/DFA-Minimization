import java.util.ArrayList;
import java.util.List;


public class DFA {
	//DFA 5-tuple
	public List<List<String>> Q = new ArrayList<List<String>>(); 
	public List<String> Alphabet = new ArrayList<String>();
	public List<ThreeTuple> TF = new ArrayList<ThreeTuple>();;
	public List<String> Qo = new ArrayList<String>();
	public List<List<String>> Final = new ArrayList<List<String>>();
	
	public DFA() {
		
	}
	
	public String  ToString(){
		StringBuilder str = new StringBuilder();
		str.append("(states, (");
		for(int j = 0; j < Q.size(); j++){
			for(int i = 0; i < Q.get(j).size(); i++){
				if(Q.get(j).size() == 1){
					str.append(Q.get(j).get(0) + ",");
				}else{
					if(i == 0){
						str.append("[");
					}
					str.append(Q.get(j).get(i));
					if(i != Q.get(j).size()-1){
						str.append(",");
					}else{
						str.append("]");
						if(j != Q.size()-1){
							str.append(",");
						}
					}
				}
			}
		}
		str.append("))");
		str.append("\n");
		str.append("(alpha, (");
		for(int i = 0; i < Alphabet.size(); i++){
			str.append(Alphabet.get(i));
			if(i != Alphabet.size()-1){
				str.append(",");
			}
		}
		str.append("))");
		str.append("\n");
		str.append("(trans-func, (");
		for(int i = 0; i < TF.size(); i++){
			if(TF.get(i).a.size() != 1){
				for(int j = 0; j < TF.get(i).a.size(); j++){
					if(j != TF.get(i).a.size()-1){
						if(j == 0){
							str.append("([" + TF.get(i).a.get(j) + ",");
						}else{
							str.append(TF.get(i).a.get(j) + ",");
						}
					}else{
						str.append(TF.get(i).a.get(j) + "],");
					}
				}
			}
			else{
				str.append("(" + TF.get(i).a.get(0) + ","); 
			}
			str.append(TF.get(i).b + ",");
			if(TF.get(i).c.size() != 1){
				for(int j = 0; j < TF.get(i).c.size(); j++){
					if(j != TF.get(i).c.size()-1){
						if(j == 0){
							str.append("[" + TF.get(i).c.get(j) + ",");
						}else{
							str.append(TF.get(i).c.get(j) + ",");
						}
					}else{
						str.append(TF.get(i).c.get(j) + "]");
					}
				}
			}
			else{
				str.append(TF.get(i).c.get(0)); 
			}
			
			if(i != TF.size()-1){
				str.append("),");
			}else{
				str.append(")");
			}
				
		}
		str.append("))");
		str.append("\n");
		str.append("(start, ");
		str.append(Qo.get(0));
		str.append(")");
		str.append("\n");
		
		str.append("(final, (");
		if(Final.size() != 0){
			for(int i = 0; i < Final.size(); i++){
				for(int j = 0; j < Final.get(i).size(); j++){
					if(Final.get(i).size() == 1){
						str.append(Final.get(i).get(0));
						if(i != Final.size()-1){
							str.append(",");
						}
					}else{
						if(j == 0){
							str.append("[");
						}
						str.append(Final.get(i).get(j));
						if(j != Final.get(i).size()-1){
							str.append(",");
						}else{
							str.append("]");
							if(i != Final.size()-1){
								str.append(",");
							}
						}
					}
					
				}
			}
		}
		else{
			str.append("[]");
		}
		str.append("))");
		str.append("\n");
		return str.toString();
	}
	
}

