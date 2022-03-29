/**
This will generate all models, blockstates and item models for each rune and for each system if that applies
The block models will based on the first of the list, in this case being the oak.
That means that the oak will serve as base for replacing the textures
**/
const fs = require('fs');

console.log("Defining constants");

const systems = ['body', 'divine', 'essence'];

const normal_runes = ['generation', 'barrier'];

const system_runes = ['cultivation', 'energy'];

const materials = {
	'oak': { texture: "block/oak_log", coreMaterial: "minecraft:oak_log", requiredMiningLevel:"minecraft:wood" },
	'birch': { texture: "block/birch_log", coreMaterial: "minecraft:birch_log", requiredMiningLevel:"minecraft:wood" },
	'spruce': { texture: "block/spruce_log", coreMaterial: "minecraft:spruce_log", requiredMiningLevel:"minecraft:wood" },
	'jungle': { texture: "block/jungle_log", coreMaterial: "minecraft:jungle_log", requiredMiningLevel:"minecraft:wood" },
	'acacia': { texture: "block/acacia_log", coreMaterial: "minecraft:acacia_log", requiredMiningLevel:"minecraft:wood" },
	'dark_oak': { texture: "block/dark_oak_log", coreMaterial: "minecraft:dark_oak_log", requiredMiningLevel:"minecraft:wood" },
	'stone': { texture: "block/stone", coreMaterial: "minecraft:stone", requiredMiningLevel:"minecraft:stone" },
	'iron': { texture: "block/iron_block", coreMaterial: "minecraft:iron_block", requiredMiningLevel:"minecraft:stone" },
	'gold': { texture: "block/gold_block", coreMaterial: "minecraft:gold_block", requiredMiningLevel:"minecraft:iron" },
	'diamond': { texture: "block/diamond_block", coreMaterial: "minecraft:diamond_block", requiredMiningLevel:"minecraft:diamond" },
	'emerald': { texture: "block/emerald_block", coreMaterial: "minecraft:emerald_block", requiredMiningLevel:"minecraft:diamond" },
	'lapis': { texture: "block/lapis_block", coreMaterial: "minecraft:lapis_block", requiredMiningLevel:"minecraft:stone" },
	'copper': { texture: "block/copper_block", coreMaterial: "minecraft:copper_block", requiredMiningLevel:"minecraft:stone" },
	'netherite': { texture: "block/netherite_block", coreMaterial: "minecraft:netherite_block", requiredMiningLevel:"minecraft:netherite" }
};


const generic_block_state =
`{
	"variants": {
		"": {
			"model":"wuxiacraft:block/$$model$$"
		}
	}
}
`;

const generic_block_item_model =
`{
	"parent":"wuxiacraft:block/$$model$$"
}
`

const generic_loot_table =
`{
 	"type": "minecraft:block",
 	"pools": [
 		{
 			"rolls": 1.0,
 			"bonus_rolls": 0.0,
 			"entries": [
 				{
 					"type": "minecraft:item",
 					"name": "wuxiacraft:$$name$$"
 				}
 			],
 			"conditions": [
 				{
 					"condition": "minecraft:survives_explosion"
 				}
 			]
 		}
 	]
 }
`

const generic_recipe =
`{
	"type":"wuxiacraft:runemaking",
	"mining_level":"$$miningLevel$$",
	"rune": $$runeNumber$$,
	"ingredient": {
		"item": "$$recipeBlock$$"
	},
	"result": {
		"item": "wuxiacraft:$$name$$"
	}
}
`

const this_dir = __dirname;

const blockstates_dir = "src/main/resources/assets/wuxiacraft/blockstates";
const block_models_dir = "src/main/resources/assets/wuxiacraft/models/block";
const item_models_dir = "src/main/resources/assets/wuxiacraft/models/item";
const loot_tables_dir = "src/main/resources/data/wuxiacraft/loot_tables/blocks";
const recipes_folder = "src/main/resources/data/wuxiacraft/recipes";

console.log("Everything is defined!");
console.log("Beginning creation loops!");

function createBlockstate(model_name, file_name) {
	console.log("\tCreating blockstate!");
	let rune_item_model = generic_block_state.replaceAll("$$model$$", model_name);
	fs.writeFileSync(this_dir + "/" + blockstates_dir + "/" + file_name, rune_item_model, {encoding:'utf8', flag:'w+'});
}

function createItemModel(model_name, file_name) {
		console.log("\tCreating item model!");
	let rune_item_model = generic_block_item_model.replaceAll("$$model$$", model_name);
	fs.writeFileSync(this_dir + "/" + item_models_dir + "/" + file_name, rune_item_model, {encoding:'utf8', flag:'w+'});
}

function createLootTables(model_name, file_name) {
		console.log("\tCreating loot tables!");
	let rune_loot_table = generic_loot_table.replaceAll("$$name$$", model_name);
	fs.writeFileSync(this_dir + "/" + loot_tables_dir + "/" + file_name, rune_loot_table, {encoding:'utf8', flag:'w+'});
}

function createRecipes(name, file_name, miningLevel, recipeBlock, runeNumber) {
		console.log("\tCreating recipes!");
	let recipe = generic_recipe.replaceAll("$$miningLevel$$", miningLevel);
	recipe = recipe.replaceAll("$$recipeBlock$$", recipeBlock);
	recipe = recipe.replaceAll("$$name$$", name);
	recipe = recipe.replaceAll("$$runeNumber$$", runeNumber);
	fs.writeFileSync(this_dir + "/" + recipes_folder + "/" + file_name, recipe, {encoding:'utf8', flag:'w+'});
}

function recreateModel(example_rune_model_file, destination_model_file, example_material, destination_material) {
		console.log("\tReplacing textures into a new model!");
		if(destination_material === example_material) return;
		let example_rune_model = fs.readFileSync( this_dir + "/" + block_models_dir + "/" + example_rune_model_file ,{encoding:'utf8', flag:'r'});
		let destination_rune_model = example_rune_model.replaceAll(materials[example_material].texture, materials[destination_material].texture);
		fs.writeFileSync(this_dir + "/" + block_models_dir + "/" + destination_model_file, destination_rune_model, {encoding:'utf8', flag:'w+'});
}

for(let material in materials) {
	for(let rune of normal_runes) {
		let model_name = material + "_" + rune +"_rune";
		let file_name = model_name + ".json";
		console.log("For " + model_name + ": ");
		createBlockstate(model_name, file_name);
		createItemModel(model_name, file_name);
		createLootTables(model_name, file_name);
		let runeNumber = normal_runes.indexOf(rune);
		createRecipes(model_name, file_name, materials[material].requiredMiningLevel, materials[material].coreMaterial, runeNumber);
		let example_rune_model_file = "oak_" + rune + "_rune.json";
		recreateModel(example_rune_model_file, file_name, 'oak', material)
	}
	for(let system of systems) {
		for(let rune of system_runes) {
			let model_name = system+"_"+material + "_" + rune +"_rune";
			let file_name = model_name + ".json";
			console.log("For " + model_name + ": ");
			createBlockstate(model_name, file_name);
			createItemModel(model_name, file_name);
			createLootTables(model_name, file_name);
			let runeNumber = normal_runes.length + systems.indexOf(system) * system_runes.length + system_runes.indexOf(rune);
			createRecipes(model_name, file_name, materials[material].requiredMiningLevel, materials[material].coreMaterial, runeNumber);
			let example_rune_model_file = system+"_oak_" + rune + "_rune.json";
			recreateModel(example_rune_model_file, file_name, 'oak', material)
		}
	}
}

console.log("Creation loops done!");