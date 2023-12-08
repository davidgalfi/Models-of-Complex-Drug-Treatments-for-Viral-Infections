package HAL.GridsAndAgents;

import java.io.Serializable;

public class AgentListNode<T> implements Serializable {
    T agent;
    int i;
    long stateID;
    AgentListNode<T> next;
    AgentListNode<T> prev;
    final AgentList<T> mySet;

    AgentListNode(AgentList<T> mySet) {
        this.mySet = mySet;
    }
    void SetAgent(T agent){
        this.agent=agent;
        this.next=((AgentBase)agent).myNodes;
        ((AgentBase)agent).myNodes=this;
        this.prev=null;

    }
    void PopNode(){
        if(((AgentBase)agent).myNodes==this){
            ((AgentBase)agent).myNodes=this.next;
        }
        if(this.next!=null){
            this.next.prev=this.prev;
        }
        if(this.prev!=null){
            this.prev.next=this.next;
        }
    }
    void DisposeAll(){
        AgentListNode remMe=this;
        while(remMe!=null){
            remMe.mySet.RemoveNode(remMe);
            remMe=remMe.next;
        }
        ((AgentBase)agent).myNodes=null;
    }
}
