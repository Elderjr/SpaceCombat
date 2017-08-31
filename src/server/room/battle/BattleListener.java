/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.room.battle;

import server.actors.Skill;
import server.actors.Spaceship;


/**
 *
 * @author elderjr
 */
public interface BattleListener {

    public long createsActorId();
    
    public void damageNotification(Spaceship target, Spaceship source, int damage);
    
    public void healNotification(Spaceship target, Spaceship source, int heal);
    
    public void deathNotification(Spaceship dead, Spaceship killer);

    public void removeUser(long userId);

    public void addSkill(Skill skill);

    public void removeSkill(Skill skill);
}
