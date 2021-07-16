<template id="account-page">
<app-frame>
<v-data-table dense :headers="headers" :items="sortedItems" class="elevation-1" :search="search" :custom-filter="filterOnlyCapsText" >
  <template v-slot:top>
       <v-text-field v-model="search" label="Buscar" class="mx-4" ></v-text-field>
      <v-toolbar flat >
        <v-toolbar-title>Usuarios</v-toolbar-title>
        <v-divider class="mx-4" inset vertical ></v-divider>
        <v-spacer></v-spacer>
        <v-dialog v-model="dialog" max-width="500px">
          <template v-slot:activator="{ on, attrs }">
            <v-btn color="primary" dark class="mb-2" v-bind="attrs" v-on="on" > Crear un Nuevo Usuarios </v-btn>
          </template>
          <v-card>
            <v-card-title>
              <span class="headline">{{ formTitle }}</span>
            </v-card-title>
            <v-card-text>
              <v-container>
                <v-row>
                  <v-col cols="12" sm="6" md="4" >
                    <v-text-field v-model="editedItem.lastname" label="Apellido Paterno" ></v-text-field>
                  </v-col>
                  <v-col cols="12" sm="6" md="4" >
                    <v-text-field v-model="editedItem.maidname" label="Apellidos Materno" ></v-text-field>
                  </v-col>
                  <v-col cols="12" sm="6" md="4" >
                    <v-text-field v-model="editedItem.firstname" label="Nombre" ></v-text-field>
                  </v-col>
                  <v-col cols="12" sm="6" md="4" >
                    <v-text-field v-model="editedItem.userId" label="Usuario" ></v-text-field>
                  </v-col>
                  <v-col cols="12" sm="6" md="4" >
                    <v-text-field type="password" v-model="editedItem.password" label="Contraseña" ></v-text-field>
                  </v-col>
                  <v-col cols="12" sm="6" md="4" >
                    <v-text-field v-model="editedItem.role" label="Rol" ></v-text-field>
                  </v-col>
                  <v-col cols="12" sm="6" md="4" >
                    <v-text-field v-model="editedItem.activeaccount" label="Activar o Desactivar" ></v-text-field>
                  </v-col>
                </v-row>
              </v-container>
            </v-card-text>
            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn color="blue darken-1" text @click="close" > Cancel </v-btn>
              <v-btn color="blue darken-1" text @click="save" > Guardar </v-btn>
            </v-card-actions>
          </v-card>
        </v-dialog>
      </v-toolbar>
    </template>
    <template v-slot:item.actions="{ item }">
      <v-icon small class="mr-2" @click="editItem(item)" > mdi-pencil </v-icon>
    </template>
    <template v-slot:item.activeaccount="{ item }">
        <v-simple-checkbox
          v-model="item.activeaccount"
          disabled
        ></v-simple-checkbox>
      </template>
    <template v-slot:no-data>
      <v-btn color="primary" @click="initialize" > Reset </v-btn>
    </template>
  </v-data-table>
</app-frame>
</template>

<script>
    Vue.component("account-page", {
        template: "#account-page",
        data: () => ({
            search: '',
            dialog: false,
            dialogDelete: false,
            headers: [
              { text: 'Apellido Paterno',align: 'start' ,value: 'lastname', sortable: false },
              { text: 'Apellido Materno', value: 'maidname', sortable: false },
              { text: 'Nombre', value: 'firstname', sortable: false },
              { text: 'Usuarios',value: 'userId', },
              { text: 'ROL', value: 'role', sortable: false },
              { text: 'Activo', value: 'activeaccount', sortable: false },
              { text: 'Actions', value: 'actions', sortable: false },
            ],
            desserts: [],
            editedIndex: -1,
            editedItem: {
              id: '',
              userId: '',
              password: '',
              role: '',
              firstname: '',
              lastname: '',
              maidname: '',
              activeaccount: ''

              
            },
            defaultItem: {
              id: '',
              userId: '',
              password: '',
              role: '',
              firstname: '',
              lastname: '',
              maidname: '',
              activeaccount: ''
            },
          }),

          computed: {
            formTitle () {
              return this.editedIndex === -1 ? 'Crear Usuario' : 'Editar Usuario'
            },

           sortedItems() {
            return this.desserts.sort((s1, s2) => new Intl.Collator('es').compare(s1.name, s2.name))
           }
          },

          watch: {
            dialog (val) { val || this.close()
            },
            dialogDelete (val) { val || this.closeDelete()
            },
          },

          created () {
            this.initialize()
          },

          methods: {

            filterOnlyCapsText (value, search) {
              return value != null &&
              search != null  &&
              typeof value === 'string' &&
              value.toString().toLocaleLowerCase().replace("á","a").replace("é", "e").replace("í", "i").replace("ó", "o").replace("ú", "u").indexOf(search) !== -1

              },

            initialize () {
              fetch("/api/auth/account")
              .then(response => response.json())
              .then(result => this.desserts = result)
            },

            editItem (item) {
              fetch(`/api/auth/account/${item.id}`)
              .then(response => response.json())
              .then(desserts => {
              this.editedIndex = this.desserts.indexOf(item)
              this.editedItem = Object.assign({}, desserts)
              this.dialog = true
              })
            },

            close () {
              this.dialog = false
              this.$nextTick(() => {
                this.editedItem = Object.assign({}, this.defaultItem)
                this.editedIndex = -1
              })
            },

            closeDelete () {
              this.dialogDelete = false
              this.$nextTick(() => {
                this.editedItem = Object.assign({}, this.defaultItem)
                this.editedIndex = -1
              })
            },

            save () {
             const updating = this.editedIndex > -1
             const id = updating ? this.editedItem.id : ""
             fetch(`/api/auth/account/${id}`, {
              method: updating ? 'PATCH' : 'POST',
              body: JSON.stringify(this.editedItem)
             })
             .then(response => response.text())
             .then(result => {
               if(result !== 'Failed'){
                 if(updating){
                    Object.assign(this.desserts[this.editedIndex], this.editedItem)
                 }else{
                   const dessert = this.editedItem
                   dessert.id = result
                   this.desserts.push(dessert)
                 }
                 this.close()
               }else{
               }
             })
             .catch(err == console.error(err) )
            }
          }
        })
  </script>