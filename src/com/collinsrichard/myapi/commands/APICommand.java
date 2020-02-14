package com.collinsrichard.myapi.commands;

import com.collinsrichard.myapi.Helper;
import com.collinsrichard.myapi.Settings;
import com.collinsrichard.myapi.objects.particles.CustomParticle;
import com.collinsrichard.myapi.objects.particles.ParticleType;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class APICommand implements CommandExecutor {
    String overrideCommand = "";
    boolean requireConsole = false;
    boolean requirePlayer = false;
    boolean requirePermission = true;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (shouldCancel(sender, command)) {
            return true;
        }

        if (sender instanceof Player) {
            final Player player = (Player) sender;

            if (args.length == 0) {
                return true;
            }

            if (args.length < 6) {
                sender.sendMessage(ChatColor.RED + "Error: /api <id> <xOff> <yOff> <zOff> <speed> <amount>");
                return true;
            }

            int id = 0;
            Float x = 10F;
            Float y = 10F;
            Float z = 10F;

            Float speed = 1F;

            int amount = 100;

            try {
                id = Integer.parseInt(args[0]);
            } catch (Exception e) {

            }

            try {
                x = Float.parseFloat(args[1]);
            } catch (Exception e) {

            }

            try {
                y = Float.parseFloat(args[2]);
            } catch (Exception e) {

            }

            try {
                z = Float.parseFloat(args[3]);
            } catch (Exception e) {

            }

            try {
                speed = Float.parseFloat(args[4]);
            } catch (Exception e) {

            }

            try {
                amount = Integer.parseInt(args[5]);
            } catch (Exception e) {

            }


            System.out.println("Particle: " + ParticleType.values()[id].getName());

            final int finalId = id;
            final Float finalX = x;
            final Float finalY = y;
            final Float finalZ = z;
            final Float finalSpeed = speed;
            final int finalAmount = amount;
            new BukkitRunnable() {
                @Override
                public void run() {

                    new CustomParticle(ParticleType.values()[finalId], player.getLocation()).xSpread(finalX).ySpread(finalY).zSpread(finalZ).speed(finalSpeed).amount(finalAmount).showAll();
                }
            }.runTaskAsynchronously(Helper.getAPI());

            /*

            ArrayList<Player> toShow = new ArrayList<Player>();
            toShow.add(player);

            CustomParticle show = CustomParticle.getRandom();
            show.displayItem(player.getLocation(), 10, 10, 10, 1F, 1000, toShow);

            System.out.println(show.getName());

            DynamicMessage message = new DynamicMessage("").add(" Welcome to ").color(ChatColor.GREEN).add("Nerds Network!").color(ChatColor.AQUA).add("    Choose an option! ").color(ChatColor.RED);
            message.add("[FORUMS]").color(ChatColor.YELLOW).style(ChatColor.BOLD).link("http://nerdsnetwork.net/").tooltip(ChatColor.GREEN + "Click to go to nerdsnetwork.net!").add(" ");
            message.add("[SERVERS]").color(ChatColor.YELLOW).style(ChatColor.BOLD).command("/servers").tooltip(ChatColor.GREEN + "Click to browser servers on " + ChatColor.GOLD + " Nerds Network!");

            message.send((Player) sender);

            */
        }

        return true;
    }


    // Simply check for all required permissions

    public boolean shouldCancel(CommandSender sender, Command command) {
        if (requireConsole && requirePlayer) {
            requireConsole = false;
            requirePlayer = false;
        }

        if (sender instanceof Player) {
            if (requireConsole) {
                sender.sendMessage(ChatColor.RED + "You must be the console to do this.");
                return true;
            } else {
                if (requirePermission) {
                    String requiredPermission = (Settings.basePerms + ((overrideCommand.equalsIgnoreCase("")) ? command.getName() : overrideCommand)).toLowerCase();

                    if (!sender.hasPermission(requiredPermission)) {
                        sender.sendMessage(ChatColor.RED + "Error: You need permission '" + requiredPermission + "' to do this.");
                        return true;
                    }
                }
            }
        } else {
            if (requirePlayer) {
                sender.sendMessage(ChatColor.RED + "You must be in game to do this.");
                return true;
            }
        }

        return false;
    }
}
