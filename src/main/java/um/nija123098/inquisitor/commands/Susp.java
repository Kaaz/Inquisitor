package um.nija123098.inquisitor.commands;

import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.NickNameChangeEvent;
import sx.blah.discord.handle.impl.events.PresenceUpdateEvent;
import sx.blah.discord.handle.impl.events.UserUpdateEvent;
import sx.blah.discord.handle.obj.Presences;
import um.nija123098.inquisitor.bot.Inquisitor;
import um.nija123098.inquisitor.command.Register;
import um.nija123098.inquisitor.context.Channel;
import um.nija123098.inquisitor.context.Guild;
import um.nija123098.inquisitor.context.Rank;
import um.nija123098.inquisitor.context.Suspicion;
import um.nija123098.inquisitor.context.User;
import um.nija123098.inquisitor.util.MessageHelper;

/**
 * Made by nija123098 on 11/12/2016
 */
@Register(name = "suspicion", suspicion = Suspicion.HERETICAL)
public class Susp {
    @Register(startup = true, rank = Rank.NONE)
    public static void setUp(){
        Inquisitor.discordClient().getDispatcher().registerListener(new Susp());
    }
    @EventSubscriber
    public void hande(UserUpdateEvent event){
        if (event.getOldUser().getName().equals(event.getNewUser().getName())){
            Suspicion.addLevel(User.getUserFromID(event.getNewUser().getID()), 1, null, false);
        }
    }
    @EventSubscriber
    public void handle(NickNameChangeEvent event){
        Suspicion.addLevel(User.getUserFromID(event.getUser().getID()), .25f, null, false);
    }
    @EventSubscriber
    public void handle(PresenceUpdateEvent event){
        User user = User.getUserFromID(event.getUser().getID());
        if (event.getNewPresence().equals(Presences.DND)){
            Suspicion.addLevel(user, .3f, null, false);
        }else if (event.getNewPresence().equals(Presences.ONLINE)){
            if (!Suspicion.isSufficient(Suspicion.LOYAL, Suspicion.getLevel(user))){
                Suspicion.addLevel(user, -.25f, null, false);
            }
        }
    }
    @Register(defaul = true, suspicious = .1f, help = "Displays the suspicion level of the user")
    public static void suspicion(User user, Channel channel, Suspicion suspicion, Guild guild, String s){
        if (s.length() == 0){
            MessageHelper.send(channel, user.discord().mention() + ", you are " + suspicion.name());
        }else{
            User u = User.getUser(s, guild);
            if (u != null){
                MessageHelper.send(channel, u.discord().getName() + "#" + u.discord().getDiscriminator() + " has been " + Suspicion.getLevel(u).name());
            }else{
                MessageHelper.send(channel, "No user has been found");
            }
        }
    }
    @Register(rank = Rank.BOT_ADMIN, help = "Sets a user's suspicion level")
    public static void set(User user, Channel channel, String s){
        float v;
        try{v = Float.parseFloat(s);
        }catch(Exception e){
            MessageHelper.send(channel, "\"" + s + "\" is not a number");
            return;
        }
        user.putData("suspicion", v + "");
        suspicion(user, channel, Suspicion.getLevel(user), null, "");
    }
}
