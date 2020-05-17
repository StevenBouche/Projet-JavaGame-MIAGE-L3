package server;

import com.corundumstudio.socketio.SocketIOClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import share.face.FactoryFace;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class ServerTest {

    Server server;
    Thread tserver;
    private Lock lock = new ReentrantLock();
    private final Condition serverStart = lock.newCondition();
    private final Condition gameFinish = lock.newCondition();
    List<SocketIOClient> idPlayer;

    @BeforeEach
    void init(){
        FactoryFace.resetInstance();
        idPlayer = new ArrayList<>();
        for(int i =0; i < 4; i++) {
            SocketIOClient s = Mockito.mock(SocketIOClient.class);
            Mockito.when(s.getSessionId()).thenReturn(UUID.randomUUID());
            idPlayer.add(s);
        }
        Server.resetInstance();
        server =  Server.getInstance();
        ConfigurationServer conf = new ConfigurationServer(20000,2,this.lock,this.serverStart,this.gameFinish);
        server.setConfiguration(conf);
        server.gameManager = Mockito.spy(server.gameManager);
   //     Mockito.doNothing().when(server.gameManager).startGame();
        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                waiting();
                return null;
            }
        }).when(server.gameManager).startGame();
        tserver = new Thread(server);
    }

    @Test
    void testServer() {

        tserver.start();

        this.lock.lock();
        try {
            this.serverStart.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.lock.unlock();

        Assertions.assertTrue(server.running);
        waiting();
        Assertions.assertEquals(Thread.State.WAITING,tserver.getState());
        for(SocketIOClient id : idPlayer) server.connectClient(id.getSessionId(),"test",0);//server.onConnect(id);
        waitingVal(2500);
        Assertions.assertEquals(0,server.nbGame );
        Mockito.verify(server.gameManager,Mockito.times(2)).startGame();

        try {
            tserver.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Assertions.assertEquals(Thread.State.TERMINATED,tserver.getState());

    }

    @Test
    void testServerInterruptWaiting() {

        tserver.start();

        this.lock.lock();
        try {
            this.serverStart.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.lock.unlock();

        Assertions.assertTrue(server.running);
        waiting();
        Assertions.assertEquals(Thread.State.WAITING,tserver.getState());
        tserver.interrupt();
        try {
            tserver.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Assertions.assertEquals(Thread.State.TERMINATED,tserver.getState());

    }

    @Test
    void testServerInterruptInGame() {

        tserver.start();

        this.lock.lock();
        try {
            this.serverStart.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.lock.unlock();

        Assertions.assertTrue(server.running);
        waiting();
        Assertions.assertEquals(Thread.State.WAITING,tserver.getState());
        for(SocketIOClient id : idPlayer) server.connectClient(id.getSessionId(),"test",0);//server.onConnect(id);
        tserver.interrupt();
        try {
            tserver.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(Thread.State.TERMINATED,tserver.getState());
    }

    @Test
    void testServerDisconnection() {

        tserver.start();

        this.lock.lock();
        try {
            this.serverStart.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.lock.unlock();

        Assertions.assertTrue(server.running);
        waiting();
        Assertions.assertEquals(Thread.State.WAITING,tserver.getState());
        Mockito.doCallRealMethod().when(server.gameManager).startGame();
        for(SocketIOClient id : idPlayer) server.connectClient(id.getSessionId(),"test",0);//server.onConnect(id);
        waitingVal(500);
        server.disconnectClient(idPlayer.get(0).getSessionId());
        try {
            tserver.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(Thread.State.TERMINATED,tserver.getState());
    }

    void waiting(){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    void waitingVal(int val){
        try {
            Thread.sleep(val);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}