package edu.uw.calebcha.hvz;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;

public class VoidGenerator extends ChunkGenerator {

	@Override
	public ChunkData generateChunkData(World world, Random random, int chunkX, int chunkZ, BiomeGrid biome) {
		ChunkData chunk = createChunkData(world);
		for(int X = 0; X < 16; X++){
			for(int Z = 0; Z < 16; Z++) {
				chunk.setBlock(X, 50, Z, Material.BEDROCK);
				
			}
		}
		return chunk;
	}
    
}
