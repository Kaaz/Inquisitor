package um.nija123098.inquisitor.context;

import sx.blah.discord.handle.obj.IGuild;
import um.nija123098.inquisitor.bot.Inquisitor;
import um.nija123098.inquisitor.util.FileHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Made by nija123098 on 11/5/2016
 */
public class Guild extends Context {
    private static final List<Guild> GUILDS;
    static {
        GUILDS = new ArrayList<Guild>();
        FileHelper.ensureFileExistence("guilds");
    }
    public static Guild getGuild(String id){
        for (Guild guild : GUILDS) {
            if (guild.getID().equals(id)){
                return guild;
            }
        }
        Guild guild = new Guild(id);
        GUILDS.add(guild);
        return guild;
    }
    public Guild(String id) {
        super("guilds", id);
    }
    public IGuild discord(){
        return Inquisitor.discordClient().getGuildByID(this.getID());
    }
}
