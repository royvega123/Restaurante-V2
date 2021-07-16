<template id="Inicio">
    <app-frame>

        <v-toolbar
                color="#333333"
                dark
        >
            <v-toolbar-title>BIENVENIDO A GUSTEAU'S</v-toolbar-title>

            <v-divider
                    class="mx-4"
                    vertical
            ></v-divider>

            <v-spacer></v-spacer>

            <v-toolbar-items class="hidden-sm-and-down">
                <v-btn text >
                    <v-list-item link href="/login" :ripple="true">
                        <v-list-item-title>  Iniciar Sesion</v-list-item-title>
                    </v-list-item>

                </v-btn>

                <v-divider vertical></v-divider>

                <v-btn text>
                     <v-list-item link href="/Registro" :ripple="true">
                                           <v-list-item-title> Registro</v-list-item-title>
                                       </v-list-item>

                </v-btn>

                <v-divider vertical></v-divider>

                <v-btn text>
                    <v-list-item-title>Acerca de</v-list-item-title>
                </v-btn>

                <v-divider vertical></v-divider>

            </v-toolbar-items>



            <v-app-bar-nav-icon></v-app-bar-nav-icon>


        </v-toolbar>








        <v-carousel
                cycle
                height="650"
                hide-delimiter-background
                show-arrows-on-hover
        >
            <v-carousel-item
                    v-for="(item,i) in items"
                    :key="i"
                    :src="item.src"
                    reverse-transition="fade-transition"
                    transition="fade-transition"
            >
                <v-row
                        class="fill-height"
                        align="center"
                        justify="center"
                >
                    <v-alert
                            color="#333333"
                            dark
                    >
                    <div class="text-h2">
                        Bienvenido a Gusteau's
                    </div>
                </v-alert>
                </v-row>
            </v-carousel-item>


            <v-sheet>
                <v-row
                        class="fill-height"
                        align="center"
                        justify="center"
                >
                    <div class="text-h2" >
                        {{ slide }} Slide
                    </div>
                </v-row>
            </v-sheet
                    >

        </v-carousel>

    </app-frame>
</template>

<script>
Vue.component("Inicio", {
template: "#Inicio",
data () {
return {
items: [
{
src: 'https://cdn.pixabay.com/photo/2017/01/26/02/06/platter-2009590_960_720.jpg',
},
{
src: 'https://cnnespanol.cnn.com/wp-content/uploads/2019/01/Galerias-nuevos-restaurantes-2019-CNN-1.jpg?quality=100&strip=info',
},
{
src: 'https://cdn.pixabay.com/photo/2015/03/26/09/54/restaurant-690569_960_720.jpg',
},
{
src: 'https://www.peru.travel/Contenido/Noticia/Imagen/pe/174/1.1/Principal/88.jpg',
},
],

slides: [
'First',
'Second',
'Third',
'Fourth',
'Fifth',
],
}
},
})
</script>
<style>
@font-face{
font-family:Ginette_PersonalUseOnly;
src:url(fuentes/Ginette_PersonalUseOnly.ttf);
}

</style>