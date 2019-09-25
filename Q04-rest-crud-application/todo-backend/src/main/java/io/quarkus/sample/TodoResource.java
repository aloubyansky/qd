package io.quarkus.sample;

import io.quarkus.panache.common.Sort;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.List;


@Path("/api")
@Produces("application/json")
@Consumes("application/json")
public class TodoResource {

    @OPTIONS
    public Response opt() {
        return Response.ok().build();
    }

    @GET
    public List<Todo> getAll() {
    	return Todo.listAll(Sort.by("order"));
    }
    
    @GET
    @Path("/{id}")
    public Todo getOne(@PathParam("id") Long id) {
    	Todo one = Todo.findById(id);
    	if(one == null) {
    		throw new WebApplicationException("Todo with id " + id + " does not exist", Status.NOT_FOUND);
    	}
    	return one;
    }

    @POST
    @Transactional
    public Response create(@Valid Todo todo) {
    	todo.persist();
    	return Response.status(Status.CREATED).entity(todo).build();
    }

    @PATCH
    @Path("/{id}")
    @Transactional
    public Response update(@Valid Todo todo, @PathParam("id") Long id) {
    	Todo existing = Todo.findById(id);
    	if(existing == null) {
    		throw new WebApplicationException("Todo with id " + id + " does not exist", Status.NOT_FOUND);    		
    	}
    	existing.completed = todo.completed;
    	existing.order = todo.order;
    	existing.title = todo.title;
    	existing.url = todo.url;
    	return Response.ok(existing).build();
    }
    
    @DELETE
    @Transactional
    public Response deleteCompleted() {
    	Todo.deleteCompleted();
    	return Response.noContent().build();
    }
    
    @DELETE
    @Transactional
    @Path("/{id}")
    public Response deleteOne(@PathParam("id") Long id) {
    	Todo existing = Todo.findById(id);
    	if(existing == null) {
    		throw new WebApplicationException("Todo with id " + id + " does not exist", Status.NOT_FOUND);    		
    	}
    	existing.delete();
    	return Response.noContent().build();
   }
}