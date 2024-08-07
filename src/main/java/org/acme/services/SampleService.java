package org.acme.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.acme.resources.CSV;
import org.acme.resources.Getfilename;
import org.acme.resources.UserProfile;
import org.acme.resources.XLS;
import org.acme.security.GenerateToken;
import org.apache.commons.io.IOUtils;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.InternalServerErrorException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.jose4j.json.internal.json_simple.JSONObject;



@Path("process")
@Produces(MediaType.TEXT_PLAIN)
public class SampleService {

    @Channel("sendrows")
    Emitter<String> reqEmitter;

    @Inject
    JsonWebToken jwt;

    @Inject
    GenerateToken generateToken;

    @POST
    @Path("login")
    @PermitAll
    public Response login(UserProfile userProfile)
    {

        if(userProfile.username.equals("admin") && userProfile.password.equals("admin"))
        {
            String[] data = {"admin"};
            String token = generateToken.getToken(data, userProfile.username);
            return Response.status(Response.Status.ACCEPTED).entity(token).build();
        }

        return Response.status(Response.Status.FORBIDDEN).entity("Error : Not Authorized").build();
    }


    @POST
    @Path("uploadfile")
    @RolesAllowed("admin")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadFile(@MultipartForm MultipartFormDataInput input, @Context SecurityContext ctx)
    {
        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
        List<InputPart> inputParts = uploadForm.get("file");
        List<Map<String, String>> data = new ArrayList<>();
        for (InputPart inputPart : inputParts) {
            try {
    
                MultivaluedMap<String, String> header = inputPart.getHeaders();
                Getfilename fGetfilename = new Getfilename();
                String fileName = fGetfilename.get(header);
                
                if (fileName.toLowerCase().endsWith("xls")) {
                    XLS xls = new XLS();
                    xls.convertToMap(inputPart.getBody());
                    data = xls.GetMap();
                }else if(fileName.toLowerCase().endsWith("csv") || fileName.toLowerCase().endsWith("txt")){
                    CSV csv = new CSV();
                    csv.convertToMap(inputPart.getBody());
                    data = csv.GetMap();
                }else{
                    throw new Exception("File " + fileName + ", Not Support Yet !!");
                }

                //process publish for queue process
                for (Map<String,String> map : data) {
                    reqEmitter.send(new JSONObject(map).toJSONString());
                }

                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        

        return Response.ok().entity("Uploaded Successfully, file on processing, please wait... Uploaded by : " + ctx.getUserPrincipal().getName()).build();
    }
    

}
