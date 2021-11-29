package control;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import model.Asiakas;
import model.dao.Dao;


@WebServlet("/asiakkaat/*")
public class Asiakkaat extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
   
    public Asiakkaat() {
        super();
    System.out.println("Asiakkaat.Asiakas()");
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Asiakkaat.doGet()");
		String pathInfo=request.getPathInfo();//haetaan kutsun polkutiedot esim. /matti
		System.out.println("polku: "+pathInfo);
		Dao dao = new Dao();
		ArrayList<Asiakas> asiakkaat;
		String strJSON="";
		if(pathInfo==null) { //Haetaan kaikki autot jos ei kauttaviivaa 
			asiakkaat = dao.listaaKaikki();
			strJSON = new JSONObject().put("asiakkaat", asiakkaat).toString();	
		}else if(pathInfo.indexOf("haeyksi")!=-1) {		//indexOf hakee, ett‰ polussa on sana "haeyksi", eli haetaan yhden asiakkaan tiedot
			String asiakas_id = pathInfo.replace("/haeyksi/", ""); //poistetaan polusta "/haeyksi/", j‰ljelle j‰‰ asiakas id	
			int id=Integer.parseInt(asiakas_id);
			//System.out.println("polku: "+id);
			Asiakas asiakas = dao.etsiAsiakas(id);//joko asiakas objekti tai null
			//System.out.println(asiakas);
			if (asiakas==null) {
				strJSON="{}";
			}else {
			JSONObject JSON = new JSONObject();
			JSON.put("asiakas_id", asiakas.getAsiakas_id());
			JSON.put("etunimi", asiakas.getEtunimi());
			JSON.put("sukunimi", asiakas.getSukunimi());
			JSON.put("puhelin", asiakas.getPuhelin());	
			JSON.put("sposti", asiakas.getSposti());	
			strJSON = JSON.toString();//palautus string
			}
			//System.out.println(strJSON);//tulostetaan string
		}else{ //Haetaan hakusanan mukaiset asiakkaat eli kauttaviiva on
			String hakusana = pathInfo.replace("/", "");
			asiakkaat = dao.listaaKaikki(hakusana);
			strJSON = new JSONObject().put("asiakkaat", asiakkaat).toString();	
		}
		
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.println(strJSON);		
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		System.out.println("Asiakkaat.doPost()");
		JSONObject jsonObj = new JsonStrToObj().convert(request); //Muutetaan kutsun mukana tuleva json-string json-objektiksi	JsonStrToObj.java /control kansiossa
		Asiakas asiakas = new Asiakas();
		asiakas.setEtunimi(jsonObj.getString("etunimi"));
		asiakas.setSukunimi(jsonObj.getString("sukunimi"));
		asiakas.setPuhelin(jsonObj.getString("puhelin"));
		asiakas.setSposti(jsonObj.getString("sposti"));
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		Dao dao = new Dao();//v‰litet‰‰n daoon lis‰‰ asiakkaaseen
		if(dao.lisaaAsiakas(asiakas)){ //metodi lisaaAsiakas palauttaa true/false
			out.println("{\"response\":1}");  //Asiakkaan lis‰‰minen onnistui {"response":1}
		}else{
			out.println("{\"response\":0}");  //Asiakkaan lis‰‰minen ep‰onnistui {"response":0}
		}	
	}

	
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Asiakkaat.doPut()");
		JSONObject jsonObj = new JsonStrToObj().convert(request); //Muutetaan kutsun mukana tuleva json-string json-objektiksi	JsonStrToObj.java /control kansiossa
		//String vanharekno = jsonObj.getString("vanharekno");//jos haluat muuttaa avainta niin vanha tieto t‰ytyy tuoda
		Asiakas asiakas = new Asiakas();
		System.out.println("jsnopbj r85 doput"+jsonObj);//ID puuttuu!!!!
		asiakas.setAsiakas_id(jsonObj.getInt("asiakas_id"));
		asiakas.setEtunimi(jsonObj.getString("etunimi"));
		asiakas.setSukunimi(jsonObj.getString("sukunimi"));
		asiakas.setPuhelin(jsonObj.getString("puhelin"));
		asiakas.setSposti(jsonObj.getString("sposti"));
		response.setContentType("application/json");
		System.out.println("do put asiakas r 84:"+ asiakas);
		PrintWriter out = response.getWriter();
		Dao dao = new Dao();//v‰litet‰‰n daoon lis‰‰ asiakkaaseen
		if(dao.muutaAsiakas(asiakas)){ //metodi lisaaAsiakas palauttaa true/false
			out.println("{\"response\":1}");  //Asiakkaan lis‰‰minen onnistui {"response":1}
		}else{
			out.println("{\"response\":0}");  //Asiakkaan lis‰‰minen ep‰onnistui {"response":0}
		}	
	}


	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Asiakkaat.doDelete()");
		
		String pathInfo = request.getPathInfo();	//haetaan kutsun polkutiedot, esim. /ABC-222		
		System.out.println("polku: "+pathInfo);
		String Strpoistettavaid = pathInfo.replace("/", "");
		int poistettavaid=Integer.parseInt(Strpoistettavaid);
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		Dao dao = new Dao();	
		if(dao.poistaAsiakas(poistettavaid)){ //metodi palauttaa true/false
			out.println("{\"response\":1}");  //Auton poistaminen onnistui {"response":1}
		}else{
			out.println("{\"response\":0}");  //Auton poistaminen ep‰onnistui {"response":0}
		}	
	}
}
