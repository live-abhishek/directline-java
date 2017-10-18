package com.hashblu.agents;

import org.springframework.web.client.RestTemplate;

/**
 * Created by abhisheks on 16-Oct-17.
 */
public abstract class AbsAgentClient implements IAgentClient {
    protected RestTemplate restTemplate;

    protected AbsAgentClient(){
        restTemplate = new RestTemplate();
    }
}
