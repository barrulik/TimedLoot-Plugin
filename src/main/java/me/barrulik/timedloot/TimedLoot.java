package me.barrulik.timedloot;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

public final class TimedLoot extends JavaPlugin implements Listener {
    TimedLoot instance;
    @Override
    public void onEnable() {
        instance = this;
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(this, this);

        System.out.println("timedLoot by Barrulik");


    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player dead = event.getEntity();
        Entity killer = event.getEntity().getKiller();
        if (killer instanceof Player) {
            Location loc = dead.getLocation().add(0,1,0);
            TNTPrimed tnt = (TNTPrimed) dead.getWorld().spawnEntity(loc, EntityType.PRIMED_TNT);
            tnt.setFuseTicks(200);
            new BukkitRunnable() {
                double time = 10;
                ArmorStand armorStand = (ArmorStand) dead.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
                @Override
                public void run() {
                    if (time<=0)
                        cancel();
                    armorStand.setCustomNameVisible(true);
                    armorStand.setVisible(false);
                    armorStand.setGravity(false);
                    armorStand.setCollidable(false);
                    armorStand.setCustomName(ChatColor.RED + "" + time + "s until explosion");
                    time-=.1;
                }
            }.runTaskTimer(this, 2, 1);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("test")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                Location loc = p.getLocation().add(0, 1, 0);
                TNTPrimed tnt = (TNTPrimed) p.getWorld().spawnEntity(loc, EntityType.PRIMED_TNT);
                tnt.setFuseTicks(200);
                new BukkitRunnable() {
                    double time = 10;
                    ArmorStand armorStand = (ArmorStand) p.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);

                    @Override
                    public void run() {
                        if (time <= 0) {
                            cancel();
                            armorStand.remove();
                        }
                        armorStand.setCustomNameVisible(true);
                        armorStand.setVisible(false);
                        armorStand.setGravity(false);
                        armorStand.setCollidable(false);
                        armorStand.setCustomName(ChatColor.RED + "" + round(time) + "s until explosion");
                        time -= .1;
                    }
                }.runTaskTimer(this, 1, 2);
            }
        }
        return true;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public double round(double num){
        return Math.round(num*100)/100.0;
    }
}

