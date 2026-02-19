package me.ipapervn.expansion;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.ipapervn.vinecoins.VineCoins;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;

/**
 * VineExpansion - PlaceholderAPI support for VineCoins
 * Identifier: %vinecoins_...%
 */
@SuppressWarnings("unused")
public class VineCoinsExpansion extends PlaceholderExpansion {

    private final DecimalFormat formatter = new DecimalFormat("#,###");

    @Override
    public @NotNull String getIdentifier() {
        return "VineCoins";
    }

    @Override
    public @NotNull String getAuthor() {
        return "iPaperVN";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        if (player == null) return "";

        // Lấy instance của plugin VineCoins
        VineCoins plugin = (VineCoins) Bukkit.getPluginManager().getPlugin("VineCoins");
        if (plugin == null) return "VineCoins Not Found";

        // Tách params: %vinecoins_coin_formatted% -> ["coin", "formatted"]
        String[] args = params.toLowerCase().split("_");
        if (args.length == 0) return null;

        String type = args[0];
        double balance;

        // Xác định loại tiền
        if (type.equals("coin")) {
            balance = plugin.getCoinManager().getBalance(player.getUniqueId());
        } else if (type.equals("mcoin")) {
            balance = plugin.getMCoinManager().getBalance(player.getUniqueId());
        } else {
            return null;
        }

        // Xử lý định dạng (nếu có hậu tố ở args[1])
        if (args.length >= 2) {
            return switch (args[1]) {
                case "formatted" -> formatter.format(balance);
                case "fixed" -> String.format("%.1f", balance);
                case "raw" -> String.valueOf(balance);
                default -> String.valueOf((int) balance);
            };
        }

        // Mặc định trả về số nguyên nếu không ghi định dạng
        return String.valueOf(balance);
    }
}