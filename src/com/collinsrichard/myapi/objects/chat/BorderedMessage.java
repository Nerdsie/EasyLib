package com.collinsrichard.myapi.objects.chat;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BorderedMessage {
	public String title;
	public ArrayList<String> list = new ArrayList<String>();
	public boolean bottomLine = true;

	public String titleColor = ChatColor.GOLD + "" + ChatColor.BOLD;
	public String borderColor = "" + ChatColor.DARK_RED + ChatColor.BOLD;
	public String lineColor = "" + ChatColor.GREEN;

	int originalToAdd = 40;

	public BorderedMessage(String t) {
		title = t;
	}

	public void sendMessage(Player p) {
		int toAdd = originalToAdd;

		toAdd += +2 - title.length();

		String topLine = "";

		for (int i = 0; i < toAdd / 2; i++) {
			topLine += "=";
		}

		topLine += "|| " + titleColor + title + borderColor + " ||";

		for (int i = 0; i < toAdd / 2; i++) {
			topLine += "=";
		}

		p.sendMessage(" ");
		p.sendMessage(borderColor + topLine);
		for (String m : list) {
			p.sendMessage(lineColor + m);
		}
		if (bottomLine) {
			String bottomLine = "";

			for (int i = 0; i < originalToAdd + 3; i++) {
				bottomLine += "=";
			}

			p.sendMessage(borderColor + bottomLine);
		}
		p.sendMessage(" ");
	}

	public void add(String message) {
		list.add(message);
	}

	public void remove(int i) {
		list.remove(i);
	}

	public void sendMessage(CommandSender sender) {
		int toAdd = originalToAdd;
		toAdd += +2 - title.length();

		String topLine = "";

		for (int i = 0; i < toAdd / 2; i++) {
			topLine += "=";
		}

		topLine += "|| " + titleColor + title + borderColor + " ||";

		for (int i = 0; i < toAdd / 2; i++) {
			topLine += "=";
		}

		sender.sendMessage(" ");
		sender.sendMessage(borderColor + topLine);
		for (String m : list) {
			sender.sendMessage(lineColor + m);
		}
		if (bottomLine) {
			String bottomLine = "";

			for (int i = 0; i < originalToAdd + 3; i++) {
				bottomLine += "=";
			}

			sender.sendMessage(borderColor + bottomLine);
		}
		sender.sendMessage(" ");

	}
}
