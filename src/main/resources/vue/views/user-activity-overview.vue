<template id="user-activity-overview">
  <app-layout>

    <div class="card bg-light mb-3">
      <div class="card-header">
        <div class="row">
          <h3 class="col-6">
            Activities
          </h3>
          <div class="col" align="right">
            <button rel="tooltip" title="Add"
                    class="btn btn-info btn-simple btn-link"
                    @click="hideForm =!hideForm">
              <i class="fa fa-plus" aria-hidden="true"></i>
            </button>
          </div>
        </div>
      </div>
    </div>


    <div class="card bg-light mb-3" :class="{ 'd-none': hideForm}">
      <div class="card-header">
        <div class="row">
          <div class="col-6"> Add Activity </div>
        </div>
      </div>

      <div class="card-body">

        <form>
          <div class="form-group">
            <label for="inputAddress">User ID</label>
            <input type="text" class="form-control" id="inputAddress" readonly v-model="userId">
          </div>
          <div class="form-group">
            <label for="activity">Activity</label>
            <select class="form-select" id="activity" v-model="formData.description">
              <option selected value="Walking">Walking</option>
              <option value="Cycling">Cycling</option>
              <option value="Running">Running</option>
              <option value="Climbing">Climbing</option>
            </select>
          </div>


          <div class="form-row">
            <div class="form-group col-md-4">
              <label for="inputTime">Duration</label>
              <input type="text" class="form-control" v-model="formData.duration" id="inputTime" placeholder="In minutes...">
            </div>

            <div class="form-group col-md-4">
              <label for="inputCal">Calories</label>
              <input type="text" class="form-control" id="inputCal" v-model="formData.calories" placeholder="Estimated calories burned...">
            </div>
          </div>

          <button @click="addActivity()" class="btn btn-primary">Add Activity</button>

        </form>
      </div>
    </div>

    <div class="card bg-light mb-3">
      <div class="card-header">
        <div class="row">
          <div class="col-6"> Activities List </div>
        </div>
      </div>

      <div class="card-body" v-if="activities.length!==0">
        <div v-for="activity in activities">
          <div class="card card-body bg-light-gray" style="margin: 15px">
            <h5 class="float-left"><b-icon-check-all class="mr-2"></b-icon-check-all>
              {{activity.description}} for {{activity.duration}} minutes
            </h5>

            <div class="col" align="right">
              <button rel="tooltip" title="Delete"
                      class="btn btn-info btn-simple btn-link"
                      @click="deleteActivity(activity, index)">
                <b-icon-trash-fill variant="secondary" class="mr-2"></b-icon-trash-fill>
              </button>
            </div>

            <span>Covered {{activity.duration}} Hrs</span>
            <span> {{activity.calories}} calories burned</span>
            <span> Started at {{activity.started}} </span>



          </div>
        </div>

      </div>
      <p style="margin-left: 20px; margin-top: 20px" v-else>No activities found! Please add by clicking the create button.</p>
    </div>







  </app-layout>
</template>

<script>
app.component("user-activity-overview",{
  template: "#user-activity-overview",
  data: () => ({
    activities: [],
    userId: null,
    hideForm :true,
    formData: []
  }),
  created() {
    this.userId = this.$javalin.pathParams["user-id"];
    axios.get(`/api/users/${this.userId}/activities`)
        .then(res => this.activities = res.data)
        .catch(() => console.log("Error while fetching activities"));

  },
  methods: {
    addActivity: function (){
      const url = `/api/activities`;
      const userId = this.$javalin.pathParams["user-id"];
      axios.post(url,
          {
            description: this.formData.description,
            duration: this.formData.duration,
            calories: this.formData.calories,
            started: new Date().toISOString(),
            userId: userId
          })
          .then(response => {
            this.activities.push(response.data)
            this.hideForm= true;
          })
          .catch(error => {
            console.log(error)
          })
    },
    deleteActivity: function (activity, index) {
      if (confirm('Are you sure you want to delete?')) {
        const url = `/api/activities/${activity.id}`;
        axios.delete(url)
            .then(response =>
                this.activities.splice(index, 1).push(response.data))
            .catch(function (error) {
              console.log(error)
            });
      }
    },
  }
});
</script>