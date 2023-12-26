<template id="user-targets">
  <app-layout>
    <div class="card bg-light mb-3">
      <div class="card-header">
        <div class="row">
          <div class="col-6">
            User Targets
          </div>

          <div class="col" align="right" v-if="targets.length === 0">
            <button rel="tooltip" title="Add"
                    class="btn btn-info btn-simple btn-link"
                    @click="hideForm =!hideForm">
              <i class="fa fa-plus" aria-hidden="true"></i>
            </button>
          </div>
          <div class="col" align="right" v-else>
            <div class="p2">
              <button rel="tooltip" title="Update" @click=onClick() class="btn btn-info btn-simple btn-link">
                <i class="fa fa-pencil" aria-hidden="true"></i>
              </button>
              <button rel="tooltip" title="Delete" class="btn btn-info btn-simple btn-link"
                      @click="deleteTarget()">
                <i class="fas fa-trash" aria-hidden="true"></i>
              </button>
            </div>

          </div>
        </div>
      </div>

      <div v-if="targets.length === 0">
        <br>
        <p style="margin-left: 15px"> No Targets added yet! Create Targets by clicking the add button.</p>
      </div>
      <div class="card-body" :class="{ 'd-none': hideForm}">
        <form id="addUserTargets">
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-target-Sleep">Target Sleep(Hr)</span>
            </div>
            <input type="text" class="form-control" v-model="formData.targetSleep" name="targetSleep" placeholder="Target Sleep"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-target-Bmi">Target BMI</span>
            </div>
            <input type="text" class="form-control" v-model="formData.targetBmi" name="targetBmi" placeholder="Target Bmi"/>
          </div>
        </form>
        <button rel="tooltip" title="Update" class="btn btn-primary float-right" @click="addUserTargets()">Add Targets</button>
      </div>
    </div>



    <div class="card bg-light mb-3" v-if="targets.length !== 0">

      <div class="card-body" v-for="(target,index) in targets" v-bind:key="index">
        <form>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-target-sleep">User ID</span>
            </div>
            <input type="text" class="form-control" v-model="target.userId" name="targetSleep" readonly/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-target-sleep">Target Sleep(Hr)</span>
            </div>
            <input type="text" class="form-control" id="targetSleep" v-model="target.targetSleep" :disabled="disabled" name="targetSleep" placeholder="Target Sleep"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-target-bmi">Target BMI</span>
            </div>
            <input type="text" class="form-control" id="targetBmi" v-model="target.targetBmi" :disabled="disabled" name="targetBmi" placeholder="Target Bmi"/>
          </div>

          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-target-level">Date</span>
            </div>
            <input type="text" class="form-control" v-model="target.date" name="targetSleep" readonly/>

          </div>
        </form>
      </div>
      <div class="card-footer text-center" v-if="!disabled">
        <button rel="tooltip" title="Update" class="btn btn-primary center" @click="updateUserTargets()">Update Targets</button>
      </div>

    </div>





  </app-layout>
</template>
<script>
app.component("user-targets", {
  template: "#user-targets",
  data: () => ({
    targets: [],
    formData: [],
    hideForm: true,
    disabled: true
  }),
  created() {
    this.getUserTargets();
  },
  methods: {
    onClick: function () {
      this.disabled=false
    },
    getUserTargets: function () {
      const userId = this.$javalin.pathParams["user-id"];
      const url = `/api/users/${userId}/targets`;
      axios.get(url)
          .then(res => this.targets = res.data)
          .catch(() => console.log("Error while fetching targets"));
    },
    deleteTarget: function () {
      if (confirm('Are you sure you want to delete?')) {
        const userId = this.$javalin.pathParams["user-id"];
        const url = `/api/users/${userId}/targets`;
        axios.delete(url)
            .then(response =>
                this.targets = [])
            .catch(function (error) {
              console.log(error)
            })
      }
    },
    addUserTargets: function (){
      const userId = this.$javalin.pathParams["user-id"];
      const url = `/api/users/${userId}/targets`;
      axios.post(url,
          {
            targetSleep: this.formData.targetSleep,
            targetBmi: this.formData.targetBmi,

            date: new Date().toISOString(),
            userId: userId
          })
          .then(response => {
            this.targets.push(response.data)
            this.hideForm= true;
            this.disabled=true;
          })
          .catch(error => {
            console.log(error)
          })
    },
    updateUserTargets: function () {
      const userId = this.$javalin.pathParams["user-id"];
      const url = `/api/users/${userId}/targets`
      const data={
        targetSleep: parseInt(document.getElementById("targetSleep").value)
        ,targetBmi: parseInt(document.getElementById("targetBmi").value),

        date: new Date().toISOString(),
        userId: userId}
      axios.patch(url,
          {
            data
          })
          .then(response =>
          {this.targets.pop()
            this.targets.push(data)
            this.disabled=true})
          .catch(error => {
            console.log(error)
          })
    },
  }
});
</script>