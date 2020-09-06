package com.airesnor.wuxiacraft.world;

import com.airesnor.wuxiacraft.world.data.WorldSectData;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.text.TextFormatting;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

// TODO - fix the error checking of this
public class Sect {

    private String sectName;
    private String sectTag;
    private String colour;
    private UUID sectLeader;
    private List<Pair<String, Integer>> ranks;
    private List<Pair<String, Integer>> defaultRanks;
    private List<Pair<UUID, String>> members;
    private List<UUID> invitations;
    private List<Pair<String, Boolean>> allies;
    private List<Pair<String, Boolean>> enemies;

    private boolean disband;
    private long disbandTime;
    private boolean changeLeader;
    private long changeLeaderTime;


    public Sect(String sectName, String sectTag, UUID sectLeaderUUID) {
        this.sectName = sectName;
        this.sectTag = sectTag;
        this.colour = "white";
        this.sectLeader = sectLeaderUUID;
        this.ranks = new ArrayList<>();
        this.defaultRanks = new ArrayList<>();
        this.members = new ArrayList<>();
        this.invitations = new ArrayList<>();
        this.allies = new ArrayList<>();
        this.enemies = new ArrayList<>();
        this.disband = false;
        this.disbandTime = 0;
        this.changeLeader = false;
        this.changeLeaderTime = 0;

        this.ranks.add(Pair.of("ServiceDisciple", 0));
        this.ranks.add(Pair.of("OuterDisciple", 1));
        this.ranks.add(Pair.of("InnerDisciple", 2));
        this.ranks.add(Pair.of("CoreDisciple", 3));
        this.ranks.add(Pair.of("SectElder", 4));
        this.ranks.add(Pair.of("ViceSectLeader", 5));

        this.defaultRanks.add(Pair.of("ServiceDisciple", 0));
        this.defaultRanks.add(Pair.of("OuterDisciple", 1));
        this.defaultRanks.add(Pair.of("InnerDisciple", 2));
        this.defaultRanks.add(Pair.of("CoreDisciple", 3));
        this.defaultRanks.add(Pair.of("SectElder", 4));
        this.defaultRanks.add(Pair.of("ViceSectLeader", 5));
    }

    public Sect() {
        this.sectName = null;
        this.sectTag = null;
        this.colour = null;
        this.sectLeader = null;
        this.ranks = new ArrayList<>();
        this.defaultRanks = new ArrayList<>();
        this.members = new ArrayList<>();
        this.invitations = new ArrayList<>();
        this.allies = new ArrayList<>();
        this.enemies = new ArrayList<>();
        this.disband = false;
        this.disbandTime = 0;
        this.changeLeader = false;
        this.changeLeaderTime = 0;

        this.ranks.add(Pair.of("ServiceDisciple", 0));
        this.ranks.add(Pair.of("OuterDisciple", 1));
        this.ranks.add(Pair.of("InnerDisciple", 2));
        this.ranks.add(Pair.of("CoreDisciple", 3));
        this.ranks.add(Pair.of("SectElder", 4));
        this.ranks.add(Pair.of("ViceSectLeader", 5));

        this.defaultRanks.add(Pair.of("ServiceDisciple", 0));
        this.defaultRanks.add(Pair.of("OuterDisciple", 1));
        this.defaultRanks.add(Pair.of("InnerDisciple", 2));
        this.defaultRanks.add(Pair.of("CoreDisciple", 3));
        this.defaultRanks.add(Pair.of("SectElder", 4));
        this.defaultRanks.add(Pair.of("ViceSectLeader", 5));
    }

    public void setSectName(String sectName) {
        this.sectName = sectName;
    }

    public String getSectName() {
        return sectName;
    }

    public void setSectTag(String sectTag) {
        this.sectTag = sectTag;
    }

    public String getSectTag() {
        return sectTag;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public TextFormatting getColour() {
        TextFormatting textFormatting = TextFormatting.WHITE;
        if (colour.equalsIgnoreCase("aqua")) {
            textFormatting = TextFormatting.AQUA;
        } else if (colour.equalsIgnoreCase("black")) {
            textFormatting = TextFormatting.BLACK;
        } else if (colour.equalsIgnoreCase("dark_aqua")) {
            textFormatting = TextFormatting.DARK_AQUA;
        } else if (colour.equalsIgnoreCase("blue")) {
            textFormatting = TextFormatting.BLUE;
        } else if (colour.equalsIgnoreCase("dark_blue")) {
            textFormatting = TextFormatting.DARK_BLUE;
        } else if (colour.equalsIgnoreCase("dark_gray")) {
            textFormatting = TextFormatting.DARK_GRAY;
        } else if (colour.equalsIgnoreCase("dark_green")) {
            textFormatting = TextFormatting.DARK_GREEN;
        } else if (colour.equalsIgnoreCase("dark_purple")) {
            textFormatting = TextFormatting.DARK_PURPLE;
        } else if (colour.equalsIgnoreCase("dark_red")) {
            textFormatting = TextFormatting.DARK_RED;
        } else if (colour.equalsIgnoreCase("gold")) {
            textFormatting = TextFormatting.GOLD;
        } else if (colour.equalsIgnoreCase("gray")) {
            textFormatting = TextFormatting.GRAY;
        } else if (colour.equalsIgnoreCase("green")) {
            textFormatting = TextFormatting.GREEN;
        } else if (colour.equalsIgnoreCase("light_purple")) {
            textFormatting = TextFormatting.LIGHT_PURPLE;
        } else if (colour.equalsIgnoreCase("red")) {
            textFormatting = TextFormatting.RED;
        } else if (colour.equalsIgnoreCase("white")) {
            textFormatting = TextFormatting.WHITE;
        } else if (colour.equalsIgnoreCase("yellow")) {
            textFormatting = TextFormatting.YELLOW;
        }
        return textFormatting;
    }

    public void setSectLeader(UUID sectLeader) {
        this.sectLeader = sectLeader;
    }

    public UUID getSectLeader() {
        return sectLeader;
    }

    public void addRank(String rankName, int permissionLevel) {
        boolean exists = false;
        for (Pair<String, Integer> rank : ranks) {
            if (rank.getLeft().equalsIgnoreCase(rankName)) {
                exists = true;
                break;
            }
        }
        if (!exists) {
            ranks.add(Pair.of(rankName, permissionLevel));
        }
        Comparator<Pair<String, Integer>> rankComparable = Comparator.comparingInt(Pair::getRight);
        ranks.sort(rankComparable);
    }

    public void removeRank(String rankName) {
        for (int i = 0; i < ranks.size(); i++) {
            Pair<String, Integer> rank = ranks.get(i);
            if (rank.getLeft().equalsIgnoreCase(rankName)) {
                ranks.remove(i);
                break;
            }
        }
    }

    public List<Pair<String, Integer>> getRanks() {
        return ranks;
    }

    public Pair<String, Integer> getRank(String rankName) {
        Pair<String, Integer> sectRank = null;
        for (Pair<String, Integer> rank : ranks) {
            if (rank.getLeft().equalsIgnoreCase(rankName)) {
                sectRank = rank;
                break;
            }
        }
        return sectRank;
    }

    public List<Pair<String, Integer>> getDefaultRanks() {
        return defaultRanks;
    }

    public void setRankPermissionLevel(String rankName, int permissionLevel) {
        for (int i = 0; i < ranks.size(); i++) {
            Pair<String, Integer> rank = ranks.get(i);
            if (rank.getLeft().equalsIgnoreCase(rankName)) {
                ranks.remove(i);
                ranks.add(i, Pair.of(rank.getLeft(), permissionLevel));
                break;
            }
        }
    }

    public void addMember(UUID memberUUID, String rankName) {
        boolean exists = false;
        for (Pair<UUID, String> member : members) {
            if (member.getLeft().equals(memberUUID)) {
                exists = true;
                break;
            }
        }
        if (!exists) {
            members.add(Pair.of(memberUUID, rankName));
            removePlayerFromInvitations(memberUUID);
        }
    }

    public void removeMember(UUID memberUUID) {
        for (int i = 0; i < members.size(); i++) {
            UUID member = members.get(i).getLeft();
            if (member.equals(memberUUID)) {
                members.remove(i);
                break;
            }
        }
    }

    public List<Pair<UUID, String>> getMembers() {
        return members;
    }

    public Pair<UUID, String> getMemberByUUID(UUID uuid) {
        Pair<UUID, String> sectMember = null;
        for (Pair<UUID, String> member : members) {
            if (member.getLeft().equals(uuid)) {
                sectMember = member;
                break;
            }
        }
        return sectMember;
    }

    public void setMemberRank(UUID uuid, String rank) {
        for (int i = 0; i < members.size(); i++) {
            Pair<UUID, String> member = members.get(i);
            if (member.getLeft().equals(uuid)) {
                members.remove(i);
                members.add(i, Pair.of(uuid, rank));
            }
        }
    }

    public void removePlayerFromInvitations(UUID playerUUID) {
        for (int i = 0; i < invitations.size(); i++) {
            if (invitations.get(i).equals(playerUUID)) {
                invitations.remove(i);
                break;
            }
        }
    }

    public void addPlayerToInvitation(UUID playerUUID) {
        boolean exists = false;
        for (UUID invitation : invitations) {
            if (invitation.equals(playerUUID)) {
                exists = true;
                break;
            }
        }
        if (!exists) {
            invitations.add(playerUUID);
        }
    }

    public List<UUID> getInvitations() {
        return invitations;
    }

    public void addAlly(String sectName, boolean remove) {
        boolean exists = false;
        for (Pair<String, Boolean> ally : allies) {
            if (ally.getLeft().equalsIgnoreCase(sectName)) {
                exists = true;
                break;
            }
        }
        if (!exists) {
            allies.add(Pair.of(sectName, remove));
        }
    }

    public void removeAlly(String sectName) {
        for (int i = 0; i < allies.size(); i++) {
            if (allies.get(i).getLeft().equalsIgnoreCase(sectName)) {
                allies.remove(i);
                break;
            }
        }
    }

    public boolean isAlly(String sectName) {
        boolean isAlly = false;
        for (Pair<String, Boolean> ally : allies) {
            if (ally.getLeft().equalsIgnoreCase(sectName)) {
                isAlly = true;
                break;
            }
        }
        return isAlly;
    }

    public List<Pair<String, Boolean>> getAllies() {
        return allies;
    }

    public void addEnemy(String sectName, boolean remove) {
        boolean exists = false;
        for (Pair<String, Boolean> enemy : enemies) {
            if (enemy.getLeft().equalsIgnoreCase(sectName)) {
                exists = true;
                break;
            }
        }
        if (!exists) {
            enemies.add(Pair.of(sectName, remove));
        }
    }

    public void removeEnemy(String sectName) {
        for (int i = 0; i < enemies.size(); i++) {
            if (enemies.get(i).getLeft().equalsIgnoreCase(sectName)) {
                enemies.remove(i);
                break;
            }
        }
    }

    public boolean isEnemy(String sectName) {
        boolean isEnemy = false;
        for (Pair<String, Boolean> enemy : enemies) {
            if (enemy.getLeft().equalsIgnoreCase(sectName)) {
                isEnemy = true;
                break;
            }
        }
        return isEnemy;
    }

    public List<Pair<String, Boolean>> getEnemies() {
        return enemies;
    }

    public void setDisband(boolean disband) {
        this.disband = disband;
    }

    public boolean isDisbanding() {
        return disband;
    }

    public void setDisbandTime(long disbandTime) {
        this.disbandTime = disbandTime;
    }

    public long getDisbandTime() {
        return disbandTime;
    }

    public void setChangeLeader(boolean changeLeader) {
        this.changeLeader = changeLeader;
    }

    public boolean isChangingLeader() {
        return changeLeader;
    }

    public void setChangeLeaderTime(long changeLeaderTime) {
        this.changeLeaderTime = changeLeaderTime;
    }

    public long getChangeLeaderTime() {
        return changeLeaderTime;
    }

    public Sect readFromNBT(NBTTagCompound nbt) {
        if (nbt == null) {
            nbt = new NBTTagCompound();
        }
        this.setSectName(nbt.getString("sectName"));
        this.setSectLeader(nbt.getUniqueId("sectLeader"));
        NBTTagList rankList = (NBTTagList) nbt.getTag("rankList");
        NBTTagList memberList = (NBTTagList) nbt.getTag("memberList");
        NBTTagList invitationList = (NBTTagList) nbt.getTag("invitationList");
        NBTTagList allyList = (NBTTagList) nbt.getTag("allyList");
        NBTTagList enemyList = (NBTTagList) nbt.getTag("enemyList");
        for (int i = 0; i < rankList.tagCount(); i++) {
            NBTTagCompound tagCompound = rankList.getCompoundTagAt(i);
            addRank(tagCompound.getString("rankName"), tagCompound.getInteger("rankPermissionLevel"));
        }
        for (int i = 0; i < memberList.tagCount(); i++) {
            NBTTagCompound tagCompound = memberList.getCompoundTagAt(i);
            addMember(tagCompound.getUniqueId("memberUUID"), tagCompound.getString("memberRank"));
        }
        for (int i = 0; i < invitationList.tagCount(); i++) {
            NBTTagCompound tagCompound = invitationList.getCompoundTagAt(i);
            addPlayerToInvitation(tagCompound.getUniqueId("playerInvitationUUID"));
        }
        for (int i = 0; i < allyList.tagCount(); i++) {
            NBTTagCompound tagCompound = allyList.getCompoundTagAt(i);
            addAlly(tagCompound.getString("allyName"), tagCompound.getBoolean("allyRemove"));
        }
        for (int i = 0; i < enemyList.tagCount(); i++) {
            NBTTagCompound tagCompound = enemyList.getCompoundTagAt(i);
            addEnemy(tagCompound.getString("enemyName"), tagCompound.getBoolean("enemyRemove"));
        }
        this.setDisband(nbt.getBoolean("disbanding"));
        this.setDisbandTime(nbt.getLong("disbandTime"));
        this.setChangeLeader(nbt.getBoolean("changeLeader"));
        this.setChangeLeaderTime(nbt.getLong("changeLeaderTime"));
        this.setSectTag(nbt.getString("sectTag"));
        this.setColour(nbt.getString("sectTagColour"));
        return this;
    }

    public NBTTagCompound writeToNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        NBTTagList rankList = new NBTTagList();
        NBTTagList memberList = new NBTTagList();
        NBTTagList invitationList = new NBTTagList();
        NBTTagList allyList = new NBTTagList();
        NBTTagList enemyList = new NBTTagList();
        tag.setString("sectName", sectName);
        tag.setUniqueId("sectLeader", sectLeader);
        for (Pair<String, Integer> rank : ranks) {
            NBTTagCompound rankCompound = new NBTTagCompound();
            rankCompound.setString("rankName", rank.getLeft());
            rankCompound.setInteger("rankPermissionLevel", rank.getRight());
            rankList.appendTag(rankCompound);
        }
        for (Pair<UUID, String> member : members) {
            NBTTagCompound memberCompound = new NBTTagCompound();
            memberCompound.setUniqueId("memberUUID", member.getLeft());
            memberCompound.setString("memberRank", member.getRight());
            memberList.appendTag(memberCompound);
        }
        for (UUID invitation : invitations) {
            NBTTagCompound invitationCompund = new NBTTagCompound();
            invitationCompund.setUniqueId("playerInvitationUUID", invitation);
            invitationList.appendTag(invitationCompund);
        }
        for (Pair<String, Boolean> ally : allies) {
            NBTTagCompound allyCompound = new NBTTagCompound();
            allyCompound.setString("allyName", ally.getLeft());
            allyCompound.setBoolean("allyRemove", ally.getRight());
            allyList.appendTag(allyCompound);
        }
        for (Pair<String, Boolean> enemy : enemies) {
            NBTTagCompound enemyCompound = new NBTTagCompound();
            enemyCompound.setString("enemyName", enemy.getLeft());
            enemyCompound.setBoolean("enemyRemove", enemy.getRight());
            enemyList.appendTag(enemyCompound);
        }
        tag.setTag("rankList", rankList);
        tag.setTag("memberList", memberList);
        tag.setTag("invitationList", invitationList);
        tag.setTag("allyList", allyList);
        tag.setTag("enemyList", enemyList);
        tag.setBoolean("disbanding", disband);
        tag.setLong("disbandTime", disbandTime);
        tag.setBoolean("changeLeader", changeLeader);
        tag.setLong("changeLeaderTime", changeLeaderTime);
        tag.setString("sectTag", sectTag);
        tag.setString("sectTagColour", colour);
        return tag;
    }

    public String getDefaultRank() {
        return ranks.get(0).getLeft();
    }

    public static Sect getSectByPlayer(EntityPlayerMP playerMP, WorldSectData sectData) {
        boolean isThis = false;
        Sect chosenSect = null;
        for (Sect sect : sectData.SECTS) {
            List<Pair<UUID, String>> members = sect.getMembers();
            UUID sectLeader = sect.getSectLeader();
            for (Pair<UUID, String> member : members) {
                if (member.getLeft().equals(playerMP.getUniqueID())) {
                    isThis = true;
                    break;
                }
            }
            if (!isThis) {
                if (sectLeader.equals(playerMP.getUniqueID())) {
                    isThis = true;
                }
            }
            if (isThis) {
                chosenSect = sect;
                break;
            }
        }
        return chosenSect;
    }

    public static Sect getSectByPlayerUUID(UUID playerUUID, WorldSectData sectData) {
        boolean isThis = false;
        Sect chosenSect = null;
        for (Sect sect : sectData.SECTS) {
            List<Pair<UUID, String>> members = sect.getMembers();
            UUID sectLeader = sect.getSectLeader();
            for (Pair<UUID, String> member : members) {
                if (member.getLeft().equals(playerUUID)) {
                    isThis = true;
                    break;
                }
            }
            if (!isThis) {
                if (sectLeader.equals(playerUUID)) {
                    isThis = true;
                }
            }
            if (isThis) {
                chosenSect = sect;
                break;
            }
        }
        return chosenSect;
    }

    public static Sect getSectByName(String sectName, WorldSectData sectData) {
        Sect chosenSect = null;
        for (Sect sect : sectData.SECTS) {
            if (sect.sectName.equalsIgnoreCase(sectName)) {
                chosenSect = sect;
                break;
            }
        }
        return chosenSect;
    }
}
