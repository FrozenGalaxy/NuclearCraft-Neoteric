package igentuman.nc.handler.config;

import igentuman.nc.setup.processors.Processors;
import igentuman.nc.setup.registration.fuel.FuelManager;
import igentuman.nc.setup.registration.materials.*;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static igentuman.nc.world.dimension.Dimensions.WASTELAIND_ID;

public class CommonConfig {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ProcessorConfig PROCESSOR_CONFIG = new ProcessorConfig(BUILDER);
    public static final OresConfig ORE_CONFIG = new OresConfig(BUILDER);
    public static final FuelConfig FUEL_CONFIG = new FuelConfig(BUILDER);
    public static final MaterialProductsConfig MATERIAL_PRODUCTS = new MaterialProductsConfig(BUILDER);
    public static final DimensionConfig DIMENSION_CONFIG = new DimensionConfig(BUILDER);
    public static final ForgeConfigSpec spec = BUILDER.build();

    private static boolean loaded = false;
    private static List<Runnable> loadActions = new ArrayList<>();

    public static void setLoaded() {
        if (!loaded)
            loadActions.forEach(Runnable::run);
        loaded = true;
    }

    public static boolean isLoaded() {
        return loaded;
    }

    public static void onLoad(Runnable action) {
        if (loaded)
            action.run();
        else
            loadActions.add(action);
    }

    public static class FuelConfig
    {
        public static ForgeConfigSpec.ConfigValue<List<Double>> HEAT;
        public static ForgeConfigSpec.ConfigValue<List<Integer>> EFFICIENCY;
        public static ForgeConfigSpec.ConfigValue<List<Integer>> DEPLETION;
        public static ForgeConfigSpec.ConfigValue<List<Integer>> CRITICALITY;
        public static ForgeConfigSpec.ConfigValue<Double> HEAT_MULTIPLIER;
        public static ForgeConfigSpec.ConfigValue<Double> DEPLETION_MULTIPLIER;

        public FuelConfig(ForgeConfigSpec.Builder builder) {
            builder.comment("Settings for reactor fuel").push("reactor_fuel");

            HEAT_MULTIPLIER = builder
                    .comment("Heat multiplier for boiling reactor.")
                    .define("heat_multiplier", 3.24444444);

            DEPLETION_MULTIPLIER = builder
                    .comment("Depletion multiplier. Affects how long fuel lasts.")
                    .defineInRange("depletion_multiplier", 10D, 0D, 100D);

            HEAT = builder
                    .comment("Base Fuel Heat: " + String.join(", ",FuelManager.initialHeat().keySet()))
                    .define("base_heat", toList(FuelManager.initialHeat().values()));
            EFFICIENCY = builder
                    .comment("Base Fuel Efficiency: " + String.join(", ",FuelManager.initialEfficiency().keySet()))
                    .define("base_efficiency", toList(FuelManager.initialEfficiency().values()));
            DEPLETION = builder
                    .comment("Base Fuel Depletion Time (seconds): " + String.join(", ",FuelManager.initialDepletion().keySet()))
                    .define("base_depletion", toList(FuelManager.initialDepletion().values()));
            CRITICALITY = builder
                    .comment("Fuel Criticality: " + String.join(", ",FuelManager.initialCriticality().keySet()))
                    .define("base_criticallity", toList(FuelManager.initialCriticality().values()));
            builder.pop();
        }

    }

    public static <T> List<T> toList(Collection<T> vals)
    {
        return new ArrayList<>(vals);
    }


    public static class MaterialProductsConfig {
        public static ForgeConfigSpec.ConfigValue<List<Boolean>> INGOTS;
        public static ForgeConfigSpec.ConfigValue<List<Boolean>> NUGGET;
        public static ForgeConfigSpec.ConfigValue<List<Boolean>> BLOCK;
        public static ForgeConfigSpec.ConfigValue<List<Boolean>> CHUNKS;
        public static ForgeConfigSpec.ConfigValue<List<Boolean>> PLATES;
        public static ForgeConfigSpec.ConfigValue<List<Boolean>> DUSTS;
        public static ForgeConfigSpec.ConfigValue<List<Boolean>> GEMS;

        public MaterialProductsConfig(ForgeConfigSpec.Builder builder) {
            builder.comment("Settings for items registration").push("material_products");

            CHUNKS = builder
                    .comment("Enable chunk registration: " + String.join(", ", Chunks.all().keySet()))
                    .define("register_chunk", Chunks.initialRegistration());

            INGOTS = builder
                    .comment("Enable ingots registration: " + String.join(", ", Ingots.all().keySet()))
                    .define("register_ingot", Ingots.initialRegistration());

            PLATES = builder
                    .comment("Enable plate registration: " + String.join(", ", Plates.all().keySet()))
                    .define("register_plate", Plates.initialRegistration());

            DUSTS = builder
                    .comment("Enable dust registration: " + String.join(", ", Dusts.all().keySet()))
                    .define("register_dust", Dusts.initialRegistration());

            NUGGET = builder
                    .comment("Enable nuggets registration: " + String.join(", ", Nuggets.all().keySet()))
                    .define("register_nugget", Nuggets.initialRegistration());

            BLOCK = builder
                    .comment("Enable blocks registration: " + String.join(", ", Blocks.all().keySet()))
                    .define("register_block", Blocks.initialRegistration());

            GEMS = builder
                    .comment("Enable gems registration: " + String.join(", ", Gems.all().keySet()))
                    .define("register_block", Gems.initialRegistration());

            builder.pop();
        }
    }

    public static class OresConfig {

        public static ForgeConfigSpec.ConfigValue<List<Integer>> ORE_AMOUNT;
        public static ForgeConfigSpec.ConfigValue<List<Integer>> ORE_VEIN_SIZE;
        public static ForgeConfigSpec.ConfigValue<List<List<Integer>>> ORE_DIMENSIONS;
        public static ForgeConfigSpec.ConfigValue<List<Integer>> ORE_MIN_HEIGHT;
        public static ForgeConfigSpec.ConfigValue<List<Integer>> ORE_MAX_HEIGHT;
        public static ForgeConfigSpec.ConfigValue<List<Boolean>> REGISTER_ORE;

        public OresConfig(ForgeConfigSpec.Builder builder) {
            builder.comment("Settings for ore generation").push("ores");

            ORE_DIMENSIONS = builder
                    .comment("List of dimensions to generate ores: " + String.join(", ", Ores.all().keySet()))
                    .define("dimensions", Ores.initialOreDimensions());

            REGISTER_ORE = builder
                    .comment("Enable ore registration: " + String.join(", ", Ores.all().keySet()))
                    .define("register_ore", Ores.initialOreRegistration());

            ORE_VEIN_SIZE = builder
                    .comment("Ore blocks per vein. Order: " + String.join(", ", Ores.all().keySet()))
                    .define("vein_size", Ores.initialOreVeinSizes());

            ORE_AMOUNT = builder
                    .comment("Veins in chunk. Order: " + String.join(", ", Ores.all().keySet()))
                    .define("veins_in_chunk", Ores.initialOreVeinsAmount());

            ORE_MIN_HEIGHT = builder
                    .comment("Minimal generation height. Order: " + String.join(", ", Ores.all().keySet()))
                    .define("min_height", Ores.initialOreMinHeight());

            ORE_MAX_HEIGHT = builder
                    .comment("Max generation height. Order: " + String.join(", ", Ores.all().keySet()))
                    .define("max_height", Ores.initialOreMaxHeight());

            builder.pop();
        }
    }

    public static class DimensionConfig {
        public final ForgeConfigSpec.ConfigValue<Boolean> registerWasteland;
        public final ForgeConfigSpec.ConfigValue<Integer> wastelandID;

        public DimensionConfig(ForgeConfigSpec.Builder builder) {
            builder.push("Dimension");
            registerWasteland = builder
                    .comment("Register Wasteland Dimension")
                    .define("wasteland", true);
            wastelandID = builder
                    .comment("Dimension ID for Wasteland")
                    .define("wastelandID", WASTELAIND_ID);
            builder.pop();
        }
    }

    public static class ProcessorConfig {
        public static ForgeConfigSpec.ConfigValue<Integer> base_time;
        public static ForgeConfigSpec.ConfigValue<Integer> base_power;
        public static ForgeConfigSpec.ConfigValue<List<Boolean>> REGISTER_PROCESSOR;
        public static ForgeConfigSpec.ConfigValue<List<Integer>> PROCESSOR_POWER;
        public static ForgeConfigSpec.ConfigValue<List<Integer>> PROCESSOR_TIME;


        public ProcessorConfig(ForgeConfigSpec.Builder builder) {
            builder.push("Processor");
            base_time = builder
                    .comment("Ticks")
                    .define("base_time", 240);
            base_power = builder
                    .comment("FE per Tick")
                    .define("base_power", 100);

            REGISTER_PROCESSOR = builder
                    .comment("Allow processor registration: " + String.join(", ", Processors.all().keySet()))
                    .define("register_processor", Processors.initialRegistered());

            PROCESSOR_POWER = builder
                    .comment("Processor power: " + String.join(", ", Processors.all().keySet()))
                    .define("processor_power", Processors.initialPower());

            PROCESSOR_TIME = builder
                    .comment("Time for processor to proceed recipe: " + String.join(", ", Processors.all().keySet()))
                    .define("processor_time", Processors.initialTime());
            builder.pop();


        }
    }
}