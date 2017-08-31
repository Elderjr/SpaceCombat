package server.network.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import server.data.BattleData;
import server.data.LobbyData;

public interface IServerRMI extends Remote {
	
	public int[] addPlayer(int spaceship) throws RemoteException;
	public void sendCommand(int playerId, int roomId, int command) throws RemoteException;
	public void removePlayer(int playerId, int roomId) throws RemoteException;
	public BattleData getBattleData(int playerId, int roomId) throws RemoteException;
	public LobbyData getLobbyData(int playerId, int roomId) throws RemoteException;
	public boolean testConnection() throws RemoteException;
}

