package ludicousspeed.simulator.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.EndTurnAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import ludicousspeed.LudicrousSpeedMod;

public class EndTurnPatches {
    @SpirePatch(clz = EndTurnAction.class, method = "update")
    public static class FastEndTurnPatch {
        @SpirePrefixPatch
        public static SpireReturn prefix(EndTurnAction action) {
            if (LudicrousSpeedMod.plaidMode) {
                AbstractDungeon.actionManager.turnHasEnded = true;
                GameActionManager.playerHpLastTurn = AbstractDungeon.player.currentHealth;

                action.isDone = true;
                return SpireReturn.Return(null);
            }

            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = GameActionManager.class, method = "callEndOfTurnActions")
    public static class NoNaturalEOT {
        @SpirePrefixPatch
        public static SpireReturn doNothing() {
            if(LudicrousSpeedMod.plaidMode) {
                System.err.println("how did we get here?");
            }
            return SpireReturn.Continue();
        }
    }
}
