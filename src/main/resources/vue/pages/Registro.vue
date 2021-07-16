<template id="Registro">
    <app-frame>
        <v-img height="250" src="https://cdn.pixabay.com/photo/2015/03/26/10/28/restaurant-691397_960_720.jpg"></v-img>
        <div class="signin-overlay pa-5">
            <v-card class="signin-card pa-5">
                <h1 class="mt-0 mb-5">Registrar</h1>

                                   <v-text-field v-model="editedItem.lastname" label="Apellido Paterno" ></v-text-field>

                                   <v-text-field v-model="editedItem.maidname" label="Apellidos Materno" ></v-text-field>

                                   <v-text-field v-model="editedItem.firstname" label="Nombre" ></v-text-field>

                                   <v-text-field v-model="editedItem.userId" label="Usuario" ></v-text-field>

                                   <v-text-field type="password" v-model="editedItem.password" label="Contraseña" ></v-text-field>


            <v-btn @click="save" depressed block large color="primary">Registrar</v-btn>
    </v-card>
</div>
        </app-frame>
        </template>
<script>
    Vue.component("Registro", {
        template: "#Registro",
        data: () => ({
            search: '',
            dialog: false,
            dialogDelete: false,
            headers: [
                 ],
            desserts: [],
            editedIndex: -1,
            editedItem: {
              id: '',
              userId: '',
              password: '',
              role: 'USER',
              firstname: '',
              lastname: '',
              maidname: '',
              activeaccount: 'false'

              
            },
            defaultItem: {
              id: '',
              userId: '',
              password: '',
              role: 'USER',
              firstname: '',
              lastname: '',
              maidname: '',
              activeaccount: 'false'
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


<style>
    .signin-overlay {
        position: fixed;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background: linear-gradient(125deg, #008ab4);
    }

    .v-sheet.v-card:not(.v-sheet--outlined).signin-card {
        max-width: 380px;
        margin: 150px auto;
        box-shadow: 0 2px 20px rgba(0, 0, 0, .3);
    }

    .signin-card h1 {
        font-weight: 400;
        color: #444;
        font-size: 28px;
        text-align: center;
    }
</style>
