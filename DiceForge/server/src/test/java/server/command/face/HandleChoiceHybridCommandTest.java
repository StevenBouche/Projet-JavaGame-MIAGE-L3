package server.command.face;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import server.command.Command;
import server.command.CommandManager;
import server.command.face.HandleChoiceHybridCommand;
import server.game.GameManager;
import server.game.INotifyEvent;
import share.choice.ChoiceBetweenRessource;
import share.eventclientserver.Events;
import share.exeption.InstanceFaceOutOfBoundException;
import share.face.FaceHybrid;
import share.face.FaceHybridEnum;
import share.face.FactoryFace;
import share.ressource.TypeRessource;

import java.util.*;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;

class HandleChoiceHybridCommandTest {

     Map<UUID, List<FaceHybrid>> listFace;
     UUID idPlayerOne;
     UUID idPlayerTwo;
     FactoryFace factoryFace;
     CommandManager managerCmd;
     GameManager manager;
     HandleChoiceHybridCommand cmd;

     ChoiceBetweenRessource chF;
    UUID lastIdPlayer;
    int currentValuePlayer;

    @BeforeEach
    void init(){
        FactoryFace.resetInstance();
        factoryFace = FactoryFace.getInstance();

        idPlayerOne = UUID.randomUUID();
        idPlayerTwo = UUID.randomUUID();
        INotifyEvent callback = Mockito.mock(INotifyEvent.class);
        manager = new GameManager(callback);
        manager = Mockito.spy(manager);
        managerCmd = (CommandManager) Mockito.spy(manager.commandManager);

        try {
            buildListFaceTest();
        } catch (InstanceFaceOutOfBoundException e) {
            e.printStackTrace();
        }

        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                handleSendEventTest(invocationOnMock);
                return null;
            }
        }).when(manager).sendEventToClient(any(UUID.class), any(Events.class), any(Object.class));


        Mockito.doNothing().when(managerCmd).onEndExecute(any(Command.class));
    //    Mockito.doNothing().when(managerCmd).triggerCommandEffectHammer(any(UUID.class),any(Integer.class));
        Mockito.doNothing().when(managerCmd).triggerCommand(any(Command.class));
        manager.notifyConnectionPlayer(idPlayerOne, "str",0);
        manager.notifyConnectionPlayer(idPlayerTwo, "str",0);

        FactoryFace.resetInstance();
        factoryFace = FactoryFace.getInstance();
    }

    private  Object testResult(InvocationOnMock a) {
        int valueFace = chF.getListRessource().get(chF.getTypeRessource());
        Mockito.verify(managerCmd).addRessourcePlayerCommand(eq(lastIdPlayer),eq(chF.getTypeRessource()),eq(valueFace));
        cmd.choice = null;
        return null;
    }

    private  Object testResultInverse(InvocationOnMock a) {
        int valueFace = chF.getListRessource().get(chF.getTypeRessource());
        Mockito.verify(managerCmd).removeRessourcePlayerCommand(eq(lastIdPlayer),eq(chF.getTypeRessource()),eq(valueFace));
        cmd.choice = null;
        return null;
    }

    private  Object handleSendEventTest(InvocationOnMock a) {
        UUID player = (UUID) a.getArguments()[0];
        ChoiceBetweenRessource ch = (ChoiceBetweenRessource) a.getArguments()[2];
        TypeRessource r = (TypeRessource) ch.getListRessource().keySet().toArray()[0];
        ch.setTypeRessource(r);
        chF = ch;
        lastIdPlayer = player;
        currentValuePlayer = manager.getGame().getPlayer(player).getInventory().getValueRessource(chF.getTypeRessource());
        cmd.notifyChoice(player,ch);
        return null;
    }

    private  void buildListFaceTest() throws InstanceFaceOutOfBoundException {
        listFace = new HashMap<>();
        listFace.put(idPlayerOne,new ArrayList<>());
        listFace.put(idPlayerTwo,new ArrayList<>());
        FactoryFace.resetInstance();
        factoryFace = FactoryFace.getInstance();
        listFace.get(idPlayerOne).add(factoryFace.getFaceHybrid(FaceHybridEnum.GOLD1_OR_SOLAR1_OR_LUNAR1));
        FactoryFace.resetInstance();
        factoryFace = FactoryFace.getInstance();
        listFace.get(idPlayerOne).add(factoryFace.getFaceHybrid(FaceHybridEnum.GOLD2_OR_SOLAR2_OR_LUNAR2));
        FactoryFace.resetInstance();
        factoryFace = FactoryFace.getInstance();
        listFace.get(idPlayerTwo).add(factoryFace.getFaceHybrid(FaceHybridEnum.GOLD2_OR_SOLAR2_OR_LUNAR2));
        FactoryFace.resetInstance();
        factoryFace = FactoryFace.getInstance();
        listFace.get(idPlayerTwo).add(factoryFace.getFaceHybrid(FaceHybridEnum.GOLD1_OR_SOLAR1_OR_LUNAR1));
    }

    private  void buildCmd() {
        cmd = new HandleChoiceHybridCommand(manager,managerCmd,listFace);
        cmd = Mockito.spy(cmd);
        Mockito.doNothing().when(cmd).waitingDecision();
        Mockito.doNothing().when(cmd).notifyDecision();
    }

    private  void buildCmdInverse() {
        cmd = new HandleChoiceHybridCommand(manager,managerCmd,listFace,true);
        cmd = Mockito.spy(cmd);
        Mockito.doNothing().when(cmd).waitingDecision();
        Mockito.doNothing().when(cmd).notifyDecision();
    }

    @Test
    public void onExecute(){

        buildCmd();

        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                testResult(invocationOnMock);
                return null;
            }
        }).when(cmd).resetChoice();

        cmd.trigger();

    }

    @Test
    public void onExecuteInverse(){

        buildCmdInverse();

        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                testResultInverse(invocationOnMock);
                return null;
            }
        }).when(cmd).resetChoice();

        cmd.trigger();

    }

    @Test
    public void onExecuteOne() throws InstanceFaceOutOfBoundException {
        FactoryFace.resetInstance();
        FaceHybrid face = FactoryFace.getInstance().getFaceHybrid(FaceHybridEnum.GOLD3_OR_GLORY2);
        cmd = new HandleChoiceHybridCommand(manager,managerCmd,idPlayerOne,face);
        cmd = Mockito.spy(cmd);
        Mockito.doNothing().when(cmd).waitingDecision();
        Mockito.doNothing().when(cmd).notifyDecision();
        ChoiceBetweenRessource ch = new ChoiceBetweenRessource();
        ch.setTypeRessource(TypeRessource.GOLD);
        cmd.notifyChoice(idPlayerOne,ch);
        cmd.trigger();
        Mockito.verify(managerCmd).addRessourcePlayerCommand(eq(idPlayerOne),eq(TypeRessource.GOLD),eq(3));
    }

    @Test
    public void onExecuteOneInverse() throws InstanceFaceOutOfBoundException {
        FactoryFace.resetInstance();
        FaceHybrid face = FactoryFace.getInstance().getFaceHybrid(FaceHybridEnum.GOLD3_OR_GLORY2);
        cmd = new HandleChoiceHybridCommand(manager,managerCmd,idPlayerOne,face,true);
        cmd = Mockito.spy(cmd);
        Mockito.doNothing().when(cmd).waitingDecision();
        Mockito.doNothing().when(cmd).notifyDecision();
        ChoiceBetweenRessource ch = new ChoiceBetweenRessource();
        ch.setTypeRessource(TypeRessource.GOLD);
        cmd.notifyChoice(idPlayerOne,ch);
        cmd.trigger();
        Mockito.verify(managerCmd).removeRessourcePlayerCommand(eq(idPlayerOne),eq(TypeRessource.GOLD),eq(3));
    }



}