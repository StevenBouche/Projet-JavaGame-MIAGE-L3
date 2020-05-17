package mainlauncher.launcher;

public interface ILauncher {

    StateLauncher getStateLauncher();
    int getPortServer();
    int getNbClient();

}
