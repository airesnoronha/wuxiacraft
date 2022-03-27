/**
This will generate all models, blockstates and item models for each rune and for each system if that applies
The block models will based on the first of the list, in this case being the oak.
That means that the oak will serve as base for replacing the textures
**/
const fs = require('fs');

console.log("Defining constants");

const systems = ['body', 'divine', 'essence'];

const normal_runes = ['barrier', 'generation'];

const system_runes = ['cultivation', 'energy'];

const materials = {
	'oak':'block/oak_log',
	'birch':'block/birch_log',
	'spruce':'block/spruce_log',
	'jungle':'block/jungle_log',
	'acacia':'block/acacia_log',
	'dark_oak':'block/dark_oak_log',
	'stone':'block/stone',
	'iron':'block/iron_block',
	'gold': 'block/gold_block',
	'diamond': 'block/diamond_block',
	'emerald':'block/emerald_block',
	'lapis': 'block/lapis_block',
	'copper': 'block/copper_block',
	'netherite':'block/netherite_block'
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

const this_dir = __dirname;

const blockstates_dir = "src/main/resources/assets/wuxiacraft/blockstates";
const block_models_dir = "src/main/resources/assets/wuxiacraft/models/block";
const item_models_dir = "src/main/resources/assets/wuxiacraft/models/item";
const loot_tables_dir = "src/main/resources/data/wuxiacraft/loot_tables/blocks";

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

function recreateModel(example_rune_model_file, destination_model_file, example_material, destination_material) {
		console.log("\tReplacing textures into a new model!");
		if(destination_material === example_material) return;
		let example_rune_model = fs.readFileSync( this_dir + "/" + block_models_dir + "/" + example_rune_model_file ,{encoding:'utf8', flag:'r'});
		let destination_rune_model = example_rune_model.replaceAll(materials[example_material], materials[destination_material]);
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
			let example_rune_model_file = system+"_oak_" + rune + "_rune.json";
			recreateModel(example_rune_model_file, file_name, 'oak', material)
		}
	}
}

console.log("Creation loops done!");