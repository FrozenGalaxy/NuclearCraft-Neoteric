package igentuman.nc.datagen.recipes.recipes;

import igentuman.nc.recipes.ingredient.NcIngredient;
import igentuman.nc.content.processors.Processors;
import igentuman.nc.content.materials.Materials;
import net.minecraft.data.recipes.FinishedRecipe;

import java.util.List;
import java.util.function.Consumer;

import static net.minecraft.world.item.Items.*;

public class RockCrusherRecipes extends AbstractRecipeProvider {

    public static void generate(Consumer<FinishedRecipe> consumer) {
        RockCrusherRecipes.consumer = consumer;
        ID = Processors.ROCK_CRUSHER;


        add(
                (ingredient(BASALT, 7)),
                List.of(dustStack(Materials.tungsten, 1), dustStack(Materials.niobium, 1))
        );

        add(
                (ingredient(ANCIENT_DEBRIS, 1)),
                List.of(NcIngredient.stack(stack(NETHERITE_SCRAP, 2)), dustStack(Materials.titanium, 1))
        );

        add(
                (ingredient(GRANITE, 4)),
                List.of(dustStack(Materials.rhodochrosite, 2), dustStack(Materials.villiaumite))
        );

        add(
                ingredient(DIORITE, 4),
                List.of(dustStack(Materials.zirconium, 2), dustStack(Materials.fluorite), dustStack(Materials.carobbiite))
        );

        add(
                ingredient(ANDESITE, 4),
                List.of(dustStack(Materials.beryllium, 2), dustStack(Materials.arsenic))
        );
        add(
                ingredient(DEEPSLATE, 7),
                List.of(dustStack(Materials.iodine, 1), dustStack(Materials.obsidian))
        );
        add(
                ingredient(TUFF, 12),
                List.of(dustStack(Materials.chromium, 1), dustStack(Materials.coal))
        );
        add(
                ingredient(CALCITE, 5),
                List.of(dustStack(Materials.calcium, 2), dustStack(Materials.potassium))
        );
    }

    private static void add(NcIngredient input, List<NcIngredient> output, double...modifiers) {
        itemsToItems(List.of(input), output, modifiers);
    }

}
