Vue.createApp({
    data() {
        return {
            account: {},
            errorToats: null,
            errorMsg: null,
        }
    },
    methods: {
        getData() {
            const urlParams = new URLSearchParams(window.location.search);
            const id = urlParams.get('id');
            axios.get(`/api/accounts/1`)
                .then((response) => {
                    //get client ifo
                    this.account = response.data;
                    this.account.transactions.sort((a, b) => b.id - a.id)
                })
                .catch((error) => {
                    // handle error
                    this.errorMsg = "Error getting data";
                    this.errorToats.show();
                })
        },
        formatDate(date) {
            return new Date(date).toLocaleDateString('en-gb');
        }
    },
    mounted() {
        this.errorToats = new bootstrap.Toast(document.getElementById('danger-toast'));
        this.getData();
    }
}).mount('#app');