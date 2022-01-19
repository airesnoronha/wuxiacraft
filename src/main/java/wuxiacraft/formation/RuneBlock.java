package wuxiacraft.formation;

import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;

public class RuneBlock extends Block {

	public RuneBlock(Properties properties) {
		super(properties);
	}

	@Override
	public void onNeighborChange(BlockState state, IWorldReader world, BlockPos pos, BlockPos neighbor) {
		super.onNeighborChange(state, world, pos, neighbor);
		if(neighbor.equals(pos.down())) {
			world.getChunk(pos).setBlockState(pos, AirBlock.getStateById(0), false);
		}
	}

	@Override
	public boolean isTransparent(BlockState state) {
		return true;
	}

}
