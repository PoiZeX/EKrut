package server;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import entity.Subscriber;
import mysql.MySqlClass;
import ocsf.server.ConnectionToClient;

public class SubscribersDbController {
	// TODO extract this to subscribersDbController
	protected static void updateSubscribersEntities(ConnectionToClient client, ArrayList<Subscriber> subscribersLst) {
		Statement stmt;
		Subscriber entity;
		try 
		{
			if(MySqlClass.getConnection() == null) return;

			stmt = MySqlClass.getConnection().createStatement();
			for(Subscriber subscriber : subscribersLst){
				// TODO change this to another build method as we learned with question mark (?)...
				stmt.executeUpdate("UPDATE subscriber SET CreditCardNumber=\""+subscriber.getCreditCardNumber()+"\", SubscriberNumber="+subscriber.getSubscriberNumber()+" WHERE id="+subscriber.getId()+";");

			}

		} catch (SQLException e) {e.printStackTrace();}
	}

	// TODO extract this to subscribersDbController
	protected static void getTable(ConnectionToClient client) {
		Statement stmt;
		Subscriber entity;
		try {
			if(MySqlClass.getConnection() == null) return;

			stmt = MySqlClass.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM subscriber;");
			while (rs.next()) {
				entity = new Subscriber(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getString(5), rs.getString(6), rs.getInt(7));
				try {
					client.sendToClient(entity); // finally send the entity
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
