<template id="app-frame">
    <v-app>
        <template v-if="$javalin.state.userInfo === null">
            <slot></slot> <!-- don't show anything to unauthenticated users -->
        </template>
        <template v-if="$javalin.state.userInfo !== null">

            <v-menu v-if="$javalin.state.userInfo" bottom left offset-y>
            <template #activator="scope">
            <v-btn icon color="black" v-on="scope.on">
                <v-icon>mdi-account-circle</v-icon>
            </v-btn>
        </template>
        <v-list class="account-list">
            <div class="signed-in-as pa-5">
                Signed in as
                <div class="current-user">{{ $javalin.state.userInfo.id }}</div>
            </div>
            <v-list-item @click="signOut">Sign out</v-list-item>
    </v-list>
</v-menu>

<v-navigation-drawer app v-model="drawer" temporary>
<permanent expand-on-hover>
    <v-list>
        <v-list-item class="px-4">
            <v-list-item-avatar>
                <img src="https://randomuser.me/api/portraits/lego/6.jpg" />
            </v-list-item-avatar>
        </v-list-item>
        <v-list-item link>
        <v-list-item link>
        <v-list-item-content>
            <v-list-item-title class="title">
                {{$javalin.state.userInfo.fullName}}
            </v-list-item-title>
            <v-list-item-title class="title">
                {{ $javalin.state.userInfo.role }}
            </v-list-item-title>
            <v-list-item-subtitle>Conectado</v-list-item-subtitle>
        </v-list-item-content>
    </v-list>

    <v-divider></v-divider>

    <v-list nav dense >
        <v-list-item link href="/" :ripple="true">
            <v-list-item-icon>
                <v-icon>mdi-folder</v-icon>
            </v-list-item-icon>
            <v-list-item-title>Inicio</v-list-item-title>
        </v-list-item>
        <v-list-item link href="/crud" :ripple="true">
            <v-list-item-icon>
                <v-icon>mdi-view-dashboard</v-icon>
            </v-list-item-icon>
            <v-list-item-title>Data-Table</v-list-item-title>
        </v-list-item>
        <v-list-item link href="/account" :ripple="true">
            <v-list-item-icon>
                <v-icon>mdi-account-multiple</v-icon>
            </v-list-item-icon>
            <v-list-item-title>Usuarios</v-list-item-title>
        </v-list-item>
    </v-list>
    <div class="pa-2">
        <v-list-item @click="signOut">Cerrar sesion</v-list-item>
</div>
</v-navigation-drawer>
<v-app-bar app dense>
<v-app-bar-nav-icon @click="drawer = !drawer"></v-app-bar-nav-icon>
<v-toolbar-title>Application</v-toolbar-title>
        </v-app-bar>

<v-container>
<v-main>
    <slot></slot>
</v-main>
</v-container>
        </template>

        </v-app>
        </template>

<script>
    Vue.component("app-frame", {
        template: "#app-frame",
        data: () => ({
            drawer: false,
            group: null,
        }),


        watch: {
            group() {
                this.drawer = false;
            },
        },
         methods: {
            signOut() {
            fetch('/api/auth/sing-out', {
                    method: "POST"
                })
                .then(result => window.location = "/")
            }
        }


    })
</script>

