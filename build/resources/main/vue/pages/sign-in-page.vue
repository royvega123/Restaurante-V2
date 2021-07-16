<template id="sign-in-page">
    <app-frame>
        <v-img height="250" src="https://cdn.pixabay.com/photo/2015/03/26/10/28/restaurant-691397_960_720.jpg"></v-img>
        <div class="signin-overlay pa-5">
            <v-card class="signin-card pa-5">
                <h1 class="mt-0 mb-5">Iniciar Sesión</h1>
                <v-alert v-model="errorAlert" dismissible type="error" class="mb-6">{{ errorMessage }}</v-alert>
                <v-text-field outlined v-model="userId" label="Usuario" append-icon="mdi-account"> </v-text-field>
                <v-text-field outlined v-model="password" label="Contraseña" type="password" @keyup.enter="signInOrUp" append-icon="mdi-lock"> </v-text-field>
            <v-btn @click="signInOrUp" depressed block large color="primary">Iniciar sesión</v-btn>
    </v-card>
</div>
        </app-frame>
        </template>
<script>
    Vue.component("sign-in-page", {
        template: "#sign-in-page",
        data: () => ({
            errorAlert: false,
            errorMessage: "",
            userId: "",
            password: "",
        }),
        methods: {
            signInOrUp() {
                this.errorAlert = false;
                fetch('/api/auth/sign-in', {
                    method: "POST",
                    body: JSON.stringify({ userId: this.userId, password: this.password })
                })
                .then( response => response.text())
                .then( result => {
                    if (result == "success") {
                        window.location = "/home-page"
                    } else {
                        this.errorMessage = result
                        this.errorAlert = true
                    }
                })
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
