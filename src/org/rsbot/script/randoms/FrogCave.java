package org.rsbot.script.randoms;

import org.rsbot.script.Random;
import org.rsbot.script.ScriptManifest;
import org.rsbot.script.wrappers.RSNPC;

/*
* Updated by Iscream Feb 22,10
*/
@ScriptManifest(authors = {"Nightmares18", "joku.rules", "Taha", "Fred"}, name = "Frog Cave", version = 2.3)
public class FrogCave extends Random {
    private RSNPC frog;
    private boolean talkedToHerald;
    private int tries;

    @Override
    public boolean activateCondition() {
        if (!game.isLoggedIn())
            return false;
        else if ((npcs.getNearest("Frog Herald") != null) && (objects.getNearest(5917) != null)) {
            sleep(random(2000, 3000));
            return (npcs.getNearest("Frog Herald") != null) && (objects.getNearest(5917) != null);
        }
        return false;
    }

    private RSNPC findFrog() {
    	for (RSNPC npc : npcs.getAll()) {
    		if (!npc.isMoving() && npc.getHeight() == -278) {
    			return npc;
    		}
    	}
        return null;
    }

    @Override
    public int loop() {
        try {
            if (!activateCondition()) {
                talkedToHerald = false;
                frog = null;
                tries = 0;
                return -1;
            }
            if (interfaces.canContinue()) {
                talkedToHerald = true;
                interfaces.clickContinue();
                return random(600, 800);
            }
            if (getMyPlayer().isMoving()) {
                return random(600, 800);
            }
            if (!talkedToHerald) {
                final RSNPC herald = npcs.getNearest("Frog Herald");
                if (calc.distanceTo(herald) < 5) {
                    if (!calc.tileOnScreen(herald.getLocation())) {
                        camera.turnToCharacter(herald);
                    }
                    herald.doAction("Talk-to");
                    return random(500, 1000);
                } else {
                    walking.walkTileMM(herald.getLocation());
                    return random(500, 700);
                }
            }
            if (frog == null) {
                frog = findFrog();
                if (frog != null) {
                    log("Princess found! ID: " + frog.getID());
                }
            }
            if (frog != null && frog.getLocation() != null) {
                if (calc.distanceTo(frog) < 5) {
                    if (!calc.tileOnScreen(frog.getLocation())) {
                        camera.turnToCharacter(frog);
                    }
                    frog.doAction("Talk-to Frog");
                    return random(900, 1000);
                } else {
                    walking.walkTileMM(frog.getLocation());
                    return random(500, 700);
                }
            } else {
                tries++;
                if (tries > 200) {
                    tries = 0;
                    talkedToHerald = false;
                }
                return random(200, 400);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return random(200, 400);
    }
}