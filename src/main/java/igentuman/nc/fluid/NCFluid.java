package igentuman.nc.fluid;

import igentuman.nc.block.NCFluidBlock;
import igentuman.nc.setup.registration.NCFluids;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidType;

import javax.annotation.Nonnull;
import java.util.function.Consumer;
import java.util.function.Function;

import static net.minecraft.world.level.block.Blocks.AIR;


public class NCFluid extends FlowingFluid
{
	private static NCFluids.FluidEntry entryStatic;
	protected final NCFluids.FluidEntry entry;

	public static NCFluid makeFluid(Function<NCFluids.FluidEntry, ? extends NCFluid> make, NCFluids.FluidEntry entry)
	{
		entryStatic = entry;
		NCFluid result = make.apply(entry);
		entryStatic = null;

		return result;
	}

	@Override
	public Vec3 getFlow(BlockGetter pBlockReader, BlockPos pPos, FluidState pFluidState) {
		if(getFluidType().getDensity() < 0) {
			return new Vec3(0, 0.1D, 0);
		}
		return super.getFlow(pBlockReader, pPos, pFluidState);
	}

	public NCFluid(NCFluids.FluidEntry entry)
	{
		this.entry = entry;
	}

	@Nonnull
	@Override
	public Item getBucket()
	{
		return entry.getBucket();
	}

	@Override
	protected boolean canBeReplacedWith(FluidState fluidState, BlockGetter blockReader, BlockPos pos, Fluid fluidIn, Direction direction)
	{
		return direction==Direction.DOWN&&!isSame(fluidIn);
	}

	@Override
	public boolean isSame(@Nonnull Fluid fluidIn)
	{
		return fluidIn==entry.getStill()||fluidIn==entry.getFlowing();
	}

	@Override
	public int getTickDelay(LevelReader p_205569_1_)
	{
		if(getFluidType().getDensity() == -1000) {
			return 0;
		}
		// viscosity delta to water (1000)
		int dW = this.getFlowing().getFluidType().getViscosity()-Fluids.WATER.getFluidType().getViscosity();
		// dW for water & lava is 5000, difference in tick delay is 25 -> 0.005 as a modifier
		double v = Math.round(5 + dW*0.005);
		return Math.max(2, (int)v);
	}

	@Override
	protected float getExplosionResistance()
	{
		return 100;
	}

	@Override
	protected void createFluidStateDefinition(Builder<Fluid, FluidState> builder)
	{
		super.createFluidStateDefinition(builder);
		for(Property<?> p : (entry==null?entryStatic: entry).properties())
			builder.add(p);
	}

	@Override
	protected BlockState createLegacyBlock(FluidState state)
	{
		BlockState result = entry.getBlock().defaultBlockState().setValue(LiquidBlock.LEVEL, getLegacyLevel(state));
		for(Property<?> prop : entry.properties())
			result = NCFluidBlock.withCopiedValue(prop, result, state);
		return result;
	}

	@Override
	public boolean isSource(FluidState state)
	{
		return state.getType()==entry.getStill();
	}

	@Override
	public int getAmount(FluidState state)
	{
		if(isSource(state))
			return 8;
		else
			return state.getValue(LEVEL);
	}

	@Override
	public FluidType getFluidType()
	{
		return entry.type().get();
	}

	@Nonnull
	@Override
	public Fluid getFlowing()
	{
		return entry.getFlowing();
	}

	@Nonnull
	@Override
	public Fluid getSource()
	{
		return entry.getStill();
	}

	@Override
	protected boolean canConvertToSource()
	{
		return false;
	}

	@Override
	protected void beforeDestroyingBlock(LevelAccessor iWorld, BlockPos blockPos, BlockState blockState)
	{

	}

	@Override
	protected int getSlopeFindDistance(LevelReader iWorldReader)
	{
		return 4;
	}

	@Override
	protected int getDropOff(LevelReader iWorldReader)
	{
		return 1;
	}

	public static Consumer<FluidType.Properties> createBuilder(int density, int viscosity)
	{
		return builder -> builder.viscosity(viscosity).density(density);
	}

	public static class Flowing extends NCFluid
	{
		public String name;
		public Flowing(NCFluids.FluidEntry entry)
		{
			super(entry);
			//name = entry.getFlowing().getFluidType().toString().split(":")[1];
		}

		@Override
		protected void createFluidStateDefinition(Builder<Fluid, FluidState> builder)
		{
			super.createFluidStateDefinition(builder);
			builder.add(LEVEL);
		}

		protected void spread(LevelAccessor pLevel, BlockPos pPos, FluidState pState) {
			if(entry.getFlowing().getFluidType().getDensity() > -1000) {
				super.spread(pLevel, pPos, pState);
				return;
			}
			BlockState blockstate = pLevel.getBlockState(pPos);
			BlockPos blockpos = pPos.above();
			BlockState blockstate1 = pLevel.getBlockState(blockpos);
			FluidState fluidstate = this.getNewLiquid(pLevel, blockpos.below(), blockstate1);
			if (this.canSpreadTo(pLevel, pPos, blockstate, Direction.UP, blockpos, blockstate1, pLevel.getFluidState(blockpos), fluidstate.getType())) {
				this.spreadTo(pLevel, blockpos, blockstate1, Direction.UP, fluidstate);
			}
		}
	}
}
