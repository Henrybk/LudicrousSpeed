package ludicrousspeed.simulator.commands;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import com.megacrit.cardcrawl.ui.buttons.SkipCardButton;

public class CardRewardSkipCommand implements Command {
    public static final CardRewardSkipCommand INSTANCE = new CardRewardSkipCommand();
    private static boolean ignoreHoverLogic = false;

    @Override
    public void execute() {
        CardRewardScreen screen = AbstractDungeon.cardRewardScreen;
		
		SkipCardButton button = ReflectionHacks.getPrivate(screen, CardRewardScreen.class, "skipButton");
		
		boolean isHidden = ReflectionHacks.getPrivate(button, SkipCardButton.class, "isHidden");
		if (isHidden) {
			System.err.println("CardRewardSkipCommand skip button should not be hidden");
			ReflectionHacks.setPrivate(button, SkipCardButton.class, "isHidden", false);
		}
		
		button.hb.clicked = true;
		
        ignoreHoverLogic = true;
        screen.update();
        ignoreHoverLogic = false;
		
		if (AbstractDungeon.isScreenUp) {
            System.err.println("CardRewardSkipCommand screen didn't close after pressing skip button");
			//AbstractDungeon.closeCurrentScreen();
        }
		
		if (button.hb.clicked) {
            System.err.println("CardRewardSkipCommand skip button should have unclicked");
        }
		
		boolean isDiscovery = ReflectionHacks.getPrivate(screen, CardRewardScreen.class, "discovery");
		if (isDiscovery && screen.discoveryCard != null) {
			System.err.println("CardRewardSkipCommand discoveryCard should be null");
		}
    }

    @Override
    public String encode() {
        JsonObject cardCommandJson = new JsonObject();

        cardCommandJson.addProperty("type", "CARD_REWARD_SKIP");

        return cardCommandJson.toString();
    }

    @Override
    public String toString() {
        return "CardRewardSkip";
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
