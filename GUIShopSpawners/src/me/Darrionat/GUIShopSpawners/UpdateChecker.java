package me.Darrionat.GUIShopSpawners;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import org.bukkit.plugin.java.JavaPlugin;

public class UpdateChecker {

	private int project = 69279;
	private URL checkURL;
	private static String newVersion = "";
	private JavaPlugin plugin;

	public UpdateChecker(JavaPlugin plugin, int projectID) {
		this.plugin = plugin;
		UpdateChecker.newVersion = plugin.getDescription().getVersion();
		this.project = projectID;
		try {
			this.checkURL = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + projectID);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	public int getProjectID() {
		return project;
	}

	public JavaPlugin getPlugin() {
		return plugin;
	}

	public static String getLatestVersion() {
		return newVersion;
	}

	public String getResourceURL() {
		return "https://www.spigotmc.org/resources/" + project;
	}

	public boolean checkForUpdates() throws Exception {
		URLConnection con = checkURL.openConnection();
		UpdateChecker.newVersion = new BufferedReader(new InputStreamReader(con.getInputStream())).readLine();
		String version = "v" + plugin.getDescription().getVersion();
		return !version.equals(newVersion);
	}
}