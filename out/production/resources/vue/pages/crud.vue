<template id="crud">
  <app-frame>
    <v-data-table dense :headers="headers" :items="sortedItems" item-key="name" sort-by="name" class="elevation-1" :search="search" :custom-filter="filterOnlyCapsText">
      <template v-slot:top>
        <v-toolbar flat>
          <v-toolbar-title>
            My CRUD
            <v-spacer></v-spacer>
            <v-text-field v-model="search" append-icon="mdi-magnify" label="Search" single-line hide-details></v-text-field>
          </v-toolbar-title>
          <v-divider class="mx-4" inset vertical></v-divider>
          <v-spacer></v-spacer>
          <v-dialog v-model="dialog" max-width="500px">
            <template v-slot:activator="{ on, attrs }">
              <v-btn color="primary" dark class="mb-2" v-bind="attrs" v-on="on">
                New Item
              </v-btn>
            </template>
            <v-card>
              <v-card-title>
                <span class="headline">{{ formTitle }}</span>
              </v-card-title>

              <v-card-text>
                <v-container>
                  <v-row>
                    <v-col cols="12" sm="6" md="4">
                      <v-text-field v-model="editedItem.name" label="Dessert name"></v-text-field>
                    </v-col>
                    <v-col cols="12" sm="6" md="4">
                      <v-text-field v-model="editedItem.calories" label="Calories"></v-text-field>
                    </v-col>
                    <v-col cols="12" sm="6" md="4">
                      <v-text-field v-model="editedItem.fat" label="Fat (g)"></v-text-field>
                    </v-col>
                    <v-col cols="12" sm="6" md="4">
                      <v-text-field v-model="editedItem.carbs" label="Carbs (g)"></v-text-field>
                    </v-col>
                    <v-col cols="12" sm="6" md="4">
                      <v-text-field v-model="editedItem.proteins" label="Protein (g)"></v-text-field>
                    </v-col>
                  </v-row>
                </v-container>
              </v-card-text>

              <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn color="blue darken-1" text @click="close">
                Cancel
              </v-btn>
              <v-btn color="blue darken-1" text @click="save"> Save </v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
      <v-dialog v-model="dialogDelete" max-width="500px">
        <v-card>
          <v-card-title class="headline">Are you sure you want to delete this item?</v-card-title>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="blue darken-1" text @click="closeDelete">Cancel</v-btn>
          <v-btn color="blue darken-1" text @click="deleteItemConfirm">OK</v-btn>
        <v-spacer></v-spacer>
      </v-card-actions>
    </v-card>
  </v-dialog>
</v-toolbar>
        </template>
<template v-slot:item.actions="{ item }">
<v-icon small class="mr-2" @click="editItem(item)"> mdi-pencil </v-icon>
<v-icon small @click="deleteItem(item)"> mdi-delete </v-icon>
        </template>
<template v-slot:no-data>
<v-btn color="primary" @click="initialize"> Reset </v-btn>
        </template>
        </v-data-table>
        </app-frame>
        </template>

<script>
    Vue.component("crud", {
        template: "#crud",
        data: () => ({
            dialog: false,
            dialogDelete: false,
            search: "",
            headers: [
                { text: "Postre (100g serving)", sortable: false, value: "name" },
                { text: "Calories", align: "center", sortable: false, value: "calories" },
                { text: "Fat (g)", align: "center", sortable: false, value: "fat" },
                { text: "Carbs (g)", align: "center", sortable: false, value: "carbs" },
                { text: "Protein (g)", align: "center", sortable: false, value: "proteins" },
                { text: "Actions", align: "center", value: "actions", sortable: false },
            ],
            desserts: [],
            editedIndex: -1,
            editedItem: { id: "", name: "", calories: 0, fat: 0, carbs: 0, proteins: 0 },
            defaultItem: { id: "", name: "", calories: 0, fat: 0, carbs: 0, proteins: 0 },
        }),

        computed: {
            formTitle() {
                return this.editedIndex === -1 ? "New Item" : "Edit Item";
            },
            sortedItems() {
                return this.desserts.sort((s1, s2) => new Intl.Collator("es").compare(s1.name, s2.name));
            },
        },

        watch: {
            dialog(val) {
                val || this.close();
            },
            dialogDelete(val) {
                val || this.closeDelete();
            },
        },

        created() {
            this.initialize();
        },

        methods: {
            initialize() {
                fetch("api/postres2")
                    .then((response) => response.json())
                    .then((result) => (this.desserts = result));
            },
            editItem(item) {
                fetch(`api/postres2/${item.id}`)
                    .then((response) => response.json())
                    .then((result) => {
                        this.editedIndex = this.desserts.indexOf(item);
                        this.editedItem = Object.assign({}, result);
                        this.dialog = true;
                    });
            },
            deleteItem(item) {
                this.editedIndex = this.desserts.indexOf(item);
                this.editedItem = Object.assign({}, item);
                this.dialogDelete = true;
            },

            deleteItemConfirm() {
                const id = this.editedItem.id;
                fetch(`/api/postres2/${id}`, { method: "DELETE" })
                    .then((response) => response.text())
                    .then((result) => {
                        if (result == "SUCCESS") {
                            this.desserts.splice(this.editedIndex, 1);
                        } else {
                            alert("No se elimino el postre");
                        }
                        this.closeDelete();
                    });
            },

            close() {
                this.dialog = false;
                this.$nextTick(() => {
                    this.editedItem = Object.assign({}, this.defaultItem);
                    this.editedIndex = -1;
                });
            },

            closeDelete() {
                this.dialogDelete = false;
                this.$nextTick(() => {
                    this.editedItem = Object.assign({}, this.defaultItem);
                    this.editedIndex = -1;
                });
            },

            save() {
                const editing = this.editedIndex > -1;
                const id = editing ? this.editedItem.id : "";
                fetch(`/api/postres2/${id}`, {
                    method: editing ? "PATCH" : "POST",
                    body: JSON.stringify(this.editedItem),
                })
                    .then((response) => response.text())
                    .then((result) => {
                        if (result != "failed") {
                            if (editing) {
                                Object.assign(this.desserts[this.editedIndex], this.editedItem);
                            } else {
                                const postre = this.editedItem;
                                postre.id = result;
                                this.desserts.push(postre);
                            }
                            this.close();
                        } else {
                            alert("No se agrego postre");
                        }
                    });
            },
        },
    });
</script>
