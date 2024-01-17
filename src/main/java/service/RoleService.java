package service;

import entity.Role;

import java.util.HashSet;
import java.util.Set;

public class RoleService {

    private Set<Role> roles = new HashSet<>();

    public Set<Role> getRoles(){
        return this.roles;
    }

    public boolean isValidRole(Role role){
        return this.getRoles().contains(role);
    }

    public void addNewRole(Role aRole) throws IllegalArgumentException{
        if(this.roles.contains(aRole)){
            throw new IllegalArgumentException("Role already exists.");
        }
        this.roles.add(aRole);
    }
    public void addNewRoles(Role... roles){
        for(Role role : roles){
            try {
                addNewRole(role);
            } catch (Exception e){
                //do nothing if role already exists
                continue;
            }
        }
    }

    public boolean deleteRole(Role aRole) throws IllegalArgumentException{
        if(!this.roles.contains(aRole)){
            throw new IllegalArgumentException("Role doesn't exist.");
        }
        this.roles.remove(aRole);
        return true;
    }

}
