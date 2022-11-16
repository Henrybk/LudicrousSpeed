package ludicrousspeed.simulator.commands;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import javassist.CannotCompileException;
import javassist.CtBehavior;

import java.util.ArrayList;

public class CardRewardSelectCommand implements Command {
    int cardIndex;
    private static boolean ignoreHoverLogic = false;

    public CardRewardSelectCommand(int cardIndex) {
        this.cardIndex = cardIndex;
    }

    public CardRewardSelectCommand(String jsonString) {
        JsonObject parsed = new JsonParser().parse(jsonString).getAsJsonObject();

        this.cardIndex = parsed.get("card_index").getAsInt();
    }

    @Override
    public void execute() {
        CardRewardScreen screen = AbstractDungeon.cardRewardScreen;
        
        AbstractCard target = screen.rewardGroup.get(cardIndex);
        
        target.hb.hovered = true;
        target.hb.clicked = true;
        
        ignoreHoverLogic = true;
        screen.update();
        ignoreHoverLogic = false;
        
        if (AbstractDungeon.isScreenUp) {
            System.err.println("CardRewardSelectCommand screen didn't close after selecting card");
            //AbstractDungeon.closeCurrentScreen();
        }
        
        if (target.hb.clicked) {
            System.err.println("CardRewardSelectCommand should have unclicked");
        }
        
        boolean isDiscovery = ReflectionHacks.getPrivate(screen, CardRewardScreen.class, "discovery");
        if (isDiscovery && !(screen.discoveryCard != null)) {
            System.err.println("CardRewardSelectCommand discoveryCard should not be null");
        }
    }

    @Override
    public String encode() {
        JsonObject cardCommandJson = new JsonObject();

        cardCommandJson.addProperty("type", "CARD_REWARD_SELECT");
        cardCommandJson.addProperty("card_index", cardIndex);

        return cardCommandJson.toString();
    }

    @Override
    public String toString() {
        return "CardRewardSelectCommand" + cardIndex;
    }

    // The Grid Select Screen checks to see where the cursor is at during update, disable
    // the check so we can fake whatever hovered card we want.
    @SpirePatch(clz = AbstractCard.class, method = "updateHoverLogic")
    public static class DisableHoverLogicPatch {
        @SpirePrefixPatch
        public static SpireReturn disableHover(AbstractCard card) {
            if (ignoreHoverLogic) {
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }
    }
}
