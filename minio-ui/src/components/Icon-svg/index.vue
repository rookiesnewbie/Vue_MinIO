<template>
  <span v-if="isElIcon">
    <!-- el-icon -->
    <i class="el-icon" :class="this.iconClass"></i>
  </span>
  <span v-else>
    <!-- svg文件 @/icons/svg/ -->
    <svg class="svg-icon" aria-hidden="true">
      <use :xlink:href="iconName" />
    </svg>
  </span>
</template>

<script>
export default {
  name: 'icon-svg',
  props: {
    iconClass: {
      type: String,
      required: true
    }
  },
  computed: {
    isElIcon() {
      if (this.iconClass.slice(0, 7) === 'el-icon') {
        return true
      }
      return false
    },
    iconName() {
      return `#icon-${this.iconClass}`
    }
  }
}
</script>
