package org.acme.resources;

import java.util.Random;

import org.acme.entity.FileStore;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import io.smallrye.reactive.messaging.annotations.Blocking;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class ProcessingRows {

    @Incoming("processrows") 
    @Blocking   
    @Transactional          
    public void process(String quoteRequest) throws InterruptedException {
        
        FileStore fileStore = new FileStore();
        fileStore.filecontent = quoteRequest;
        fileStore.persist();
    }
}
