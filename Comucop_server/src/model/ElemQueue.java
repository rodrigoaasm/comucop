/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import org.json.simple.JSONObject;

/**
 *
 * @author Rodrigo Maia
 */
public class ElemQueue {
    private ClientConRecord client;
    private JSONObject jsonReq;

    public ElemQueue(ClientConRecord client, JSONObject jsonReq) {
        this.client = client;
        this.jsonReq = jsonReq;
    }

    public ClientConRecord getClient() {
        return client;
    }

    public void setClient(ClientConRecord client) {
        this.client = client;
    }

    public JSONObject getJsonReq() {
        return jsonReq;
    }

    public void setJsonReq(JSONObject jsonReq) {
        this.jsonReq = jsonReq;
    }
    
    
}
